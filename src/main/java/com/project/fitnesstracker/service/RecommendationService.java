package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.RecommendationRequest;
import com.project.fitnesstracker.dto.RecommendationResponse;
import com.project.fitnesstracker.entity.Activity;
import com.project.fitnesstracker.entity.Recommendation;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.repositories.ActivityRepository;
import com.project.fitnesstracker.repositories.RecommendationRepositories;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepositories recommendationRepositories;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public RecommendationResponse generateRecommendation(RecommendationRequest request) {

        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + request.getUserID()));

        Activity activity = activityRepository.findById(request.getActivityID())
                .orElseThrow(() ->
                        new RuntimeException("Activity not found: " + request.getActivityID()));

        if (activity.getUser().getId() != request.getUserID()) {
            throw new RuntimeException("Unauthorized - activity does not belong to user");
        }

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .activity(activity)
                .suggestions(request.getSuggestions())
                .improvements(request.getImprovements())
                .safety(request.getSafety())
                .recommendation(request.getRecommendation())
                .type(request.getType())
                .build();

        return mapToResponse(recommendationRepositories.save(recommendation));
    }

    public RecommendationResponse getRecommendationById(Long id, Long userId) {

        Recommendation recommendation = recommendationRepositories.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recommendation not found"));

        if (recommendation.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToResponse(recommendation);
    }

    public List<RecommendationResponse> getUserRecommendations(Long userId) {

        return recommendationRepositories.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecommendationResponse> getActivityRecommendations(
            Long activityId,
            Long userId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Activity not found"));

        if (activity.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        return recommendationRepositories.findByActivityId(activityId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteRecommendation(Long id, Long userId) {

        Recommendation recommendation = recommendationRepositories.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recommendation not found"));

        if (recommendation.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        recommendationRepositories.delete(recommendation);
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {

        RecommendationResponse response = new RecommendationResponse();

        response.setRecommendationId(recommendation.getId());
        response.setUserId(recommendation.getUser().getId());
        response.setActivityId(recommendation.getActivity().getId());
        response.setRecommendation(recommendation.getRecommendation());
        response.setType(recommendation.getType());
        response.setImprovements(recommendation.getImprovements());
        response.setSuggestions(recommendation.getSuggestions());
        response.setSafety(recommendation.getSafety());
        response.setCreatedAt(recommendation.getCreatedAt());
        response.setUpdatedAt(recommendation.getUpdatedAt());

        return response;
    }

    public List<RecommendationResponse> getAllRecommendations() {

        return recommendationRepositories.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}