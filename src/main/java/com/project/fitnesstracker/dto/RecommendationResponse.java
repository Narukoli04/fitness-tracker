package com.project.fitnesstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RecommendationResponse {


        private Long recommendationId;
        private Long userId;
        private Long activityId;

        private String recommendation;
        private String type;

        private List<String> improvements;
        private List<String> suggestions;
        private List<String> safety;
    }

