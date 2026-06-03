package com.project.fitnesstracker.dto;


import com.project.fitnesstracker.entity.Activitytype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponce {


    private String id;

    private String userId;

    private Activitytype type;

    private Map<String, Object> additionalMetrics;
    private Integer duration;
    private Integer caloriesburn;
    private LocalDateTime startTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
