package com.vocedelposto.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocedelposto.backend.model.Place;
import com.vocedelposto.backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OverpassService {

    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter";

    @Autowired
    private PlaceRepository placeRepository;

    private final WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(10 * 1024 * 1024)) // 10MB
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Place> fetchAndSavePlaces(double lat, double lon, double radiusMeters) {
        String query = buildQuery(lat, lon, radiusMeters);
        String response = webClient.post()
                .uri(OVERPASS_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("data=" + java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Overpass response: " + response);

        List<Place> places = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode elements = root.get("elements");

            if (elements != null) {
                for (JsonNode element : elements) {
                    System.out.println("Element id: " + element.get("id") + " type: " + element.get("type"));

                    JsonNode tags = element.get("tags");
                    if (tags == null) {
                        System.out.println("Skipping - no tags");
                        continue;
                    }

                    String osmId = element.get("id").asText();
                    if (placeRepository.findByOsmId(osmId).isPresent()) {
                        System.out.println("Skipping - already exists: " + osmId);
                        continue;
                    }

                    String name = tags.has("name") ? tags.get("name").asText() : null;
                    System.out.println("Name: " + name);
                    if (name == null) continue;

                    double elat, elon;
                    if (element.has("lat")) {
                        elat = element.get("lat").asDouble();
                        elon = element.get("lon").asDouble();
                    } else if (element.has("center")) {
                        elat = element.get("center").get("lat").asDouble();
                        elon = element.get("center").get("lon").asDouble();
                    } else {
                        System.out.println("Skipping - no coordinates for: " + name);
                        continue;
                    }

                    String category = detectCategory(tags);

                    Place place = new Place();
                    place.setName(name);
                    place.setLatitude(elat);
                    place.setLongitude(elon);
                    place.setCategory(category);
                    place.setOsmId(osmId);

                    String street = tags.has("addr:street") ? tags.get("addr:street").asText() : "";
                    String number = tags.has("addr:housenumber") ? tags.get("addr:housenumber").asText() : "";
                    String city = tags.has("addr:city") ? tags.get("addr:city").asText() : "";
                    if (!street.isEmpty()) {
                        place.setAddress(street + (number.isEmpty() ? "" : " " + number) + (city.isEmpty() ? "" : ", " + city));
                    }

                    if (tags.has("phone")) place.setPhone(tags.get("phone").asText());
                    if (tags.has("website")) place.setWebsite(tags.get("website").asText());
                    if (tags.has("opening_hours")) place.setOpeningHours(tags.get("opening_hours").asText());
                    if (tags.has("description")) place.setDescription(tags.get("description").asText());

                    System.out.println("Saving place: " + name);
                    placeRepository.save(place);
                    places.add(place);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return places;
    }

    private String buildQuery(double lat, double lon, double radiusMeters) {
        return "[out:json][timeout:25];" +
                "(" +
                "node[\"amenity\"~\"restaurant|bar|cafe|fast_food|pub\"](around:" + radiusMeters + "," + lat + "," + lon + ");" +
                "way[\"amenity\"~\"restaurant|bar|cafe|fast_food|pub\"](around:" + radiusMeters + "," + lat + "," + lon + ");" +
                ");" +
                "out center 50;";
    }

    private String detectCategory(JsonNode tags) {
        if (tags.has("amenity")) {
            String amenity = tags.get("amenity").asText();
            switch (amenity) {
                case "restaurant": return "ristorante";
                case "bar": return "bar";
                case "cafe": return "caffetteria";
                case "fast_food": return "fast food";
                case "pub": return "pub";
            }
        }
        if (tags.has("shop")) return "negozio";
        return "altro";
    }
}