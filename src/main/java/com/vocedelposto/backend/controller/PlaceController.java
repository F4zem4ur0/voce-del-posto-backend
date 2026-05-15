package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.model.Place;
import com.vocedelposto.backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeRepository.save(place);
    }

    @GetMapping("/nearby")
    public List<Place> getNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "1.0") double radius) {
        return placeRepository.findNearby(lat, lon, radius);
    }
}