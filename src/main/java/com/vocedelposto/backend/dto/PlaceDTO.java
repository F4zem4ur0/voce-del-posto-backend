package com.vocedelposto.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {
    private Long id;
    private String name;
    private String address;
    private String category;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private String openingHours;
    private String description;
}