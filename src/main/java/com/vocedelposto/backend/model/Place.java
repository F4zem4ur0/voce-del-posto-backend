package com.vocedelposto.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "places")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String address;

    @Column
    private String category; // es. "ristorante", "bar", "negozio"

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String osmId; // ID da OpenStreetMap

    @Column
    private String phone;

    @Column
    private String website;

    @Column(length = 500)
    private String openingHours;

    @Column(length = 500)
    private String description;
}