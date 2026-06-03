package com.project.fitnesstracker.repositories;

import com.project.fitnesstracker.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepositories extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserId(Long userId);

    List<Recommendation> findByActivityId(Long activityId);
}
