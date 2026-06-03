package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.RecommendationRequest;
import com.project.fitnesstracker.entity.Activity;
import com.project.fitnesstracker.entity.Recommendation;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.repositories.ActivityRepository;
import com.project.fitnesstracker.repositories.RecommendationRepositories;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepositories recommendationRepositories;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;


    public Recommendation generateRecommendation(RecommendationRequest request) {
        User user=userRepository.findById(request.getUserID())
                .orElseThrow(()-> new RuntimeException("User not Found :"+request.getUserID()));


        Activity activity=activityRepository.findById(request.getActivityID())
                .orElseThrow(()-> new RuntimeException("Activity not Found :"+request.getActivityID()));

        Recommendation recommendation= Recommendation.builder()
                .user(user)
                .activity(activity)
                .suggestions(request.getSuggestions())
                .improvements(request.getImprovements())
                .safety(request.getSafety())
                .recommendation(request.getRecommendation())
                .type(request.getType())
                .build();

        return recommendationRepositories.save(recommendation);






    }

    public List<Recommendation> getUserRecommendations(Long userId) {
        return recommendationRepositories.findByUserId(userId);

    }

    public List<Recommendation> getActivityRecommendations(Long activityId) {
        return recommendationRepositories.findByActivityId(activityId);
    }
}
