package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.dto.DTOMapper;
import com.vocedelposto.backend.dto.PlaceDTO;
import com.vocedelposto.backend.model.Place;
import com.vocedelposto.backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(DTOMapper::toPlaceDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PlaceDTO createPlace(@RequestBody Place place) {
        return DTOMapper.toPlaceDTO(placeRepository.save(place));
    }

    @GetMapping("/nearby")
    public List<PlaceDTO> getNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "1.0") double radius) {
        return placeRepository.findNearby(lat, lon, radius)
                .stream()
                .map(DTOMapper::toPlaceDTO)
                .collect(Collectors.toList());
    }
}