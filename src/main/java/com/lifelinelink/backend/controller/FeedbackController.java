package com.lifelinelink.backend.controller;

import com.lifelinelink.backend.entity.Feedback;
import com.lifelinelink.backend.entity.User;
import com.lifelinelink.backend.repository.FeedbackRepository;
import com.lifelinelink.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody Map<String, Object> feedbackData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOptional.get();
        Boolean isAnonymous = (Boolean) feedbackData.getOrDefault("anonymous", false);
        
        Feedback feedback = new Feedback(
                isAnonymous ? null : user,
                (String) feedbackData.get("category"),
                Integer.parseInt(feedbackData.get("rating").toString()),
                (String) feedbackData.get("subject"),
                (String) feedbackData.get("message"),
                (String) feedbackData.get("contactEmail"),
                (Boolean) feedbackData.getOrDefault("allowContact", false),
                isAnonymous
        );

        feedbackRepository.save(feedback);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Feedback submitted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-feedback")
    public ResponseEntity<?> getUserFeedback() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOptional.get();
        List<Feedback> userFeedback = feedbackRepository.findByUser(user);
        
        List<Map<String, Object>> result = userFeedback.stream().map(feedback -> {
            Map<String, Object> feedbackInfo = new HashMap<>();
            feedbackInfo.put("id", feedback.getId());
            feedbackInfo.put("category", feedback.getCategory());
            feedbackInfo.put("rating", feedback.getRating());
            feedbackInfo.put("subject", feedback.getSubject());
            feedbackInfo.put("message", feedback.getMessage());
            feedbackInfo.put("status", feedback.getStatus());
            feedbackInfo.put("adminResponse", feedback.getAdminResponse());
            feedbackInfo.put("createdAt", feedback.getCreatedAt());
            feedbackInfo.put("updatedAt", feedback.getUpdatedAt());
            return feedbackInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentFeedback(@RequestParam(defaultValue = "10") int limit) {
        List<Feedback> recentFeedback = feedbackRepository.findByOrderByCreatedAtDesc()
                .stream()
                .filter(feedback -> !feedback.getIsAnonymous()) // Only show non-anonymous feedback
                .limit(limit)
                .collect(Collectors.toList());
        
        List<Map<String, Object>> result = recentFeedback.stream().map(feedback -> {
            Map<String, Object> feedbackInfo = new HashMap<>();
            feedbackInfo.put("id", feedback.getId());
            feedbackInfo.put("category", feedback.getCategory());
            feedbackInfo.put("rating", feedback.getRating());
            feedbackInfo.put("subject", feedback.getSubject());
            feedbackInfo.put("message", feedback.getMessage().length() > 100 ? 
                feedback.getMessage().substring(0, 100) + "..." : feedback.getMessage());
            feedbackInfo.put("userName", feedback.getUser() != null ? feedback.getUser().getFullName() : "Anonymous");
            feedbackInfo.put("createdAt", feedback.getCreatedAt());
            return feedbackInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getFeedbackStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Double averageRating = feedbackRepository.getAverageRating();
        stats.put("averageRating", averageRating != null ? averageRating : 0.0);
        
        Long totalFeedback = feedbackRepository.count();
        stats.put("totalFeedback", totalFeedback);
        
        Long pendingFeedback = feedbackRepository.countByStatus("PENDING");
        stats.put("pendingFeedback", pendingFeedback);
        
        Long resolvedFeedback = feedbackRepository.countByStatus("RESOLVED");
        stats.put("resolvedFeedback", resolvedFeedback);

        return ResponseEntity.ok(stats);
    }
}
