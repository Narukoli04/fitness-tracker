package com.project.fitnesstracker.controller;

import com.project.fitnesstracker.dto.ActivityRequest;
import com.project.fitnesstracker.dto.ActivityResponce;
import com.project.fitnesstracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/activities")
public class ActivitiesController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponce> trackActivity(
            Authentication authentication,
            @RequestBody ActivityRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        request.setUserId(String.valueOf(userId));
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponce>> getUserActivities(
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(activityService.getUseractivities(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponce> getActivityById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(activityService.getActivityById(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponce> updateActivity(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody ActivityRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(activityService.updateActivity(id, userId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActivity(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        activityService.deleteActivity(id, userId);
        return ResponseEntity.ok("Activity deleted successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(activityService.getUserStats(userId));
    }
}