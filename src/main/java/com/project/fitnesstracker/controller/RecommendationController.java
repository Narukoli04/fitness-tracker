package com.project.fitnesstracker.controller;

import com.project.fitnesstracker.dto.RecommendationRequest;
import com.project.fitnesstracker.entity.Recommendation;
import com.project.fitnesstracker.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<Recommendation> generateRecommendation(
            @RequestBody RecommendationRequest request

    ){
        Recommendation recommendation=recommendationService.generateRecommendation(request);
                return ResponseEntity.ok(recommendation);


    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(
@PathVariable Long userId
    ){
       List <Recommendation> recommendationList=recommendationService.getUserRecommendations(userId);
        return ResponseEntity.ok(recommendationList);


    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Recommendation>> getActivityRecommendations(
            @PathVariable Long activityId
    ){
        List <Recommendation> recommendationList=recommendationService.getActivityRecommendations(activityId);
        return ResponseEntity.ok(recommendationList);


    }
}
