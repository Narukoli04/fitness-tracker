package com.project.fitnesstracker.controller;

import com.project.fitnesstracker.dto.RecommendationRequest;
import com.project.fitnesstracker.dto.RecommendationResponse;
import com.project.fitnesstracker.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<RecommendationResponse> generateRecommendation(
            Authentication authentication,
            @RequestBody RecommendationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        request.setUserID(userId);
        return ResponseEntity.ok(recommendationService.generateRecommendation(request));
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getUserRecommendations(
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<RecommendationResponse>> getActivityRecommendations(
            Authentication authentication,
            @PathVariable Long activityId) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(recommendationService.getActivityRecommendations(activityId, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponse> getRecommendationById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(recommendationService.getRecommendationById(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecommendation(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        recommendationService.deleteRecommendation(id, userId);
        return ResponseEntity.ok("Recommendation deleted successfully");
    }
}