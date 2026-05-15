package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.model.Review;
import com.vocedelposto.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/place/{placeId}")
    public List<Review> getReviewsByPlace(@PathVariable Long placeId) {
        return reviewRepository.findByPlaceId(placeId);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review saved = reviewRepository.save(review);
        return ResponseEntity.ok(saved);
    }
}