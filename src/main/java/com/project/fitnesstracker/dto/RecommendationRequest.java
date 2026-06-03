package com.project.fitnesstracker.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private long userID;

    private Long activityID;
    private String recommendation;
    private String type;

    private List<String> improvements;
    private List<String>suggestions;
    private List<String>safety;

}
