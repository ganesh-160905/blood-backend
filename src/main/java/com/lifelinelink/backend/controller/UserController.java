package com.lifelinelink.backend.controller;

import com.lifelinelink.backend.entity.User;
import com.lifelinelink.backend.entity.BloodDonation;
import com.lifelinelink.backend.repository.UserRepository;
import com.lifelinelink.backend.repository.BloodDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodDonationRepository bloodDonationRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("firstName", user.getFirstName());
            profile.put("lastName", user.getLastName());
            profile.put("email", user.getEmail());
            profile.put("phone", user.getPhone());
            profile.put("bloodType", user.getBloodType());
            profile.put("age", user.getAge());
            profile.put("city", user.getCity());
            profile.put("state", user.getState());
            profile.put("totalDonations", user.getTotalDonations());
            profile.put("points", user.getPoints());
            profile.put("isAvailable", user.getIsAvailable());
            profile.put("lastDonationDate", user.getLastDonationDate());
            profile.put("createdAt", user.getCreatedAt());
            
            return ResponseEntity.ok(profile);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody Map<String, Object> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (updates.containsKey("firstName")) {
                user.setFirstName((String) updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                user.setLastName((String) updates.get("lastName"));
            }
            if (updates.containsKey("phone")) {
                user.setPhone((String) updates.get("phone"));
            }
            if (updates.containsKey("city")) {
                user.setCity((String) updates.get("city"));
            }
            if (updates.containsKey("state")) {
                user.setState((String) updates.get("state"));
            }
            if (updates.containsKey("isAvailable")) {
                user.setIsAvailable((Boolean) updates.get("isAvailable"));
            }
            
            userRepository.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/donations")
    public ResponseEntity<?> getUserDonations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<BloodDonation> donations = bloodDonationRepository.findByUserOrderByDonationDateDesc(user);
            return ResponseEntity.ok(donations);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalDonations", user.getTotalDonations());
            stats.put("points", user.getPoints());
            stats.put("lastDonationDate", user.getLastDonationDate());
            stats.put("bloodType", user.getBloodType());
            stats.put("isAvailable", user.getIsAvailable());
            
            // Calculate next eligible donation date (56 days after last donation)
            if (user.getLastDonationDate() != null) {
                stats.put("nextEligibleDate", user.getLastDonationDate().plusDays(56));
            }
            
            return ResponseEntity.ok(stats);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }
}
