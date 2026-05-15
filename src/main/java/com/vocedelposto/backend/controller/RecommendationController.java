package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.model.AppUser;
import com.vocedelposto.backend.model.Place;
import com.vocedelposto.backend.model.Review;
import com.vocedelposto.backend.repository.AppUserRepository;
import com.vocedelposto.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Place>> getRecommendations(@PathVariable Long userId) {

        // 1. Trovo l'utente e i suoi tag preferiti
        Optional<AppUser> userOpt = appUserRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AppUser user = userOpt.get();

        if (user.getPreferredTags() == null || user.getPreferredTags().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // 2. Trovo tutti gli utenti con almeno un tag in comune
        Set<Long> myTagIds = user.getPreferredTags()
                .stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toSet());

        List<AppUser> allUsers = appUserRepository.findAll();
        List<AppUser> similarUsers = allUsers.stream()
                .filter(u -> !u.getId().equals(userId))
                .filter(u -> u.getPreferredTags() != null &&
                        u.getPreferredTags().stream()
                                .anyMatch(tag -> myTagIds.contains(tag.getId())))
                .collect(Collectors.toList());

        if (similarUsers.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // 3. Trovo i posti recensiti positivamente (rating >= 4) da utenti simili
        Map<Place, Long> placeScore = new HashMap<>();
        for (AppUser similarUser : similarUsers) {
            List<Review> reviews = reviewRepository.findByUserId(similarUser.getId());
            for (Review review : reviews) {
                if (review.getRating() >= 4) {
                    placeScore.merge(review.getPlace(), 1L, Long::sum);
                }
            }
        }

        // 4. Ordino per score e restituisco
        List<Place> recommended = placeScore.entrySet().stream()
                .sorted(Map.Entry.<Place, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recommended);
    }
}