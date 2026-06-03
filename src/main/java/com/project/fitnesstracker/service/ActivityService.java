package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.ActivityRequest;
import com.project.fitnesstracker.dto.ActivityResponce;
import com.project.fitnesstracker.entity.Activity;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.repositories.ActivityRepository;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityResponce trackActivity(ActivityRequest request) {

        User user = userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + request.getUserId()));

        Activity activity = Activity.builder()
                .user(user)
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesburn(request.getCaloriesburn())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        return mapToResponse(activityRepository.save(activity));
    }

    public ActivityResponce getActivityById(Long id, Long userId) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Activity not found"));

        if (activity.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToResponse(activity);
    }

    public ActivityResponce updateActivity(
            Long id,
            Long userId,
            ActivityRequest request) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Activity not found"));

        if (activity.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        if (request.getType() != null) {
            activity.setType(request.getType());
        }

        if (request.getDuration() != null) {
            activity.setDuration(request.getDuration());
        }

        if (request.getCaloriesburn() != null) {
            activity.setCaloriesburn(request.getCaloriesburn());
        }

        if (request.getStartTime() != null) {
            activity.setStartTime(request.getStartTime());
        }

        if (request.getAdditionalMetrics() != null) {
            activity.setAdditionalMetrics(request.getAdditionalMetrics());
        }

        return mapToResponse(activityRepository.save(activity));
    }

    public void deleteActivity(Long id, Long userId) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Activity not found"));

        if (activity.getUser().getId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        activityRepository.delete(activity);
    }

    public List<ActivityResponce> getUseractivities(Long userId) {

        return activityRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getUserStats(Long userId) {

        List<Activity> activities = activityRepository.findByUserId(userId);

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalActivities", activities.size());

        stats.put(
                "totalCaloriesBurned",
                activities.stream()
                        .mapToInt(a ->
                                a.getCaloriesburn() != null
                                        ? a.getCaloriesburn()
                                        : 0)
                        .sum()
        );

        stats.put(
                "totalDuration",
                activities.stream()
                        .mapToInt(a ->
                                a.getDuration() != null
                                        ? a.getDuration()
                                        : 0)
                        .sum()
        );

        stats.put(
                "activityByType",
                activities.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getType().name(),
                                Collectors.counting()
                        ))
        );

        return stats;
    }

    private ActivityResponce mapToResponse(Activity activity) {

        ActivityResponce response = new ActivityResponce();

        response.setId(String.valueOf(activity.getId()));
        response.setUserId(String.valueOf(activity.getUser().getId()));
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesburn(activity.getCaloriesburn());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }
}