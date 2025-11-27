package com.lifelinelink.backend.repository;

import com.lifelinelink.backend.entity.Feedback;
import com.lifelinelink.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByUser(User user);
    
    List<Feedback> findByCategory(String category);
    
    List<Feedback> findByStatus(String status);
    
    List<Feedback> findByOrderByCreatedAtDesc();
    
    @Query("SELECT f FROM Feedback f WHERE f.rating >= :minRating")
    List<Feedback> findByRatingGreaterThanEqual(@Param("minRating") Integer minRating);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double getAverageRating();
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.status = :status")
    Long countByStatus(@Param("status") String status);
}
