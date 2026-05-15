package com.vocedelposto.backend.dto;

import com.vocedelposto.backend.model.Place;
import com.vocedelposto.backend.model.Review;
import java.util.stream.Collectors;

public class DTOMapper {

    public static PlaceDTO toPlaceDTO(Place place) {
        return new PlaceDTO(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getCategory(),
                place.getLatitude(),
                place.getLongitude()
        );
    }

    public static ReviewDTO toReviewDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getPlace().getId(),
                review.getPlace().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getTags() == null ? null :
                        review.getTags().stream()
                        .map(tag -> tag.getName())
                        .collect(Collectors.toSet())
        );
    }
}