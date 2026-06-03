package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.ActivityRequest;
import com.project.fitnesstracker.dto.ActivityResponce;
import com.project.fitnesstracker.entity.Activity;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.repositories.ActivityRepository;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;


    public ActivityResponce trackActivity(ActivityRequest request) {
        User user=userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(()->new RuntimeException("invalid user"+request.getUserId()));
        Activity activity=Activity.builder()
                .user(user)
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesburn(request.getCaloriesburn())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity=activityRepository.save(activity);
        return  mapToResponse (savedActivity);
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
    public List<ActivityResponce> getUseractivities(Long  userId) {
List<Activity>activityList=activityRepository.findByUserId(userId);
return  activityList.stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
    }
}
