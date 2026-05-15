package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.dto.DTOMapper;
import com.vocedelposto.backend.dto.ReviewDTO;
import com.vocedelposto.backend.model.Review;
import com.vocedelposto.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/place/{placeId}")
    public List<ReviewDTO> getReviewsByPlace(@PathVariable Long placeId) {
        return reviewRepository.findByPlaceId(placeId)
                .stream()
                .map(DTOMapper::toReviewDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review) {
        Review saved = reviewRepository.save(review);
        return ResponseEntity.ok(DTOMapper.toReviewDTO(saved));
    }
}