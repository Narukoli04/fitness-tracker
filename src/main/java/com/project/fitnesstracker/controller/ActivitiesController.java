package com.project.fitnesstracker.controller;

import com.project.fitnesstracker.dto.ActivityRequest;
import com.project.fitnesstracker.dto.ActivityResponce;
import com.project.fitnesstracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/activities")
public class ActivitiesController {
    private final ActivityService activityService;


    @PostMapping
    public ResponseEntity <ActivityResponce>trackActivity(@RequestBody ActivityRequest  request){

        return ResponseEntity .ok(activityService.trackActivity(request));
    }
    @GetMapping
    public ResponseEntity<List<ActivityResponce>>getUseractivities(
            @RequestHeader(value="X-User-ID")
            Long  UserId
    ){

        return ResponseEntity.ok(activityService.getUseractivities(UserId));
    }

}
