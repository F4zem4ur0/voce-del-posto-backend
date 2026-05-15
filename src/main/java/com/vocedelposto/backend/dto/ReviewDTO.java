package com.vocedelposto.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long placeId;
    private String placeName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Set<String> tagNames;
}