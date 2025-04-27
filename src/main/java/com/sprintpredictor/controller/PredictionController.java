// src/main/java/com/sprintpredictor/controller/PredictionController.java
package com.sprintpredictor.controller;

import com.sprintpredictor.entity.User;
import com.sprintpredictor.security.services.UserDetailsImpl;
import com.sprintpredictor.service.PredictionService;
import com.sprintpredictor.service.PredictionService.PredictionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    private User getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());
    }

    @GetMapping("/velocity")
    public ResponseEntity<Integer> predictNextSprintVelocity(Authentication authentication) {
        User user = getCurrentUser(authentication);
        int velocity = predictionService.predictNextSprintVelocity(user);
        return ResponseEntity.ok(velocity);
    }

    @GetMapping("/capacity")
    public ResponseEntity<Integer> predictNextSprintCapacity(Authentication authentication) {
        User user = getCurrentUser(authentication);
        int capacity = predictionService.predictNextSprintCapacity(user);
        return ResponseEntity.ok(capacity);
    }

    @GetMapping("/completion-probability")
    public ResponseEntity<PredictionResult> predictCompletionProbability(
            @RequestParam int plannedPoints,
            @RequestParam int teamCapacity,
            Authentication authentication) {
        User user = getCurrentUser(authentication);
        PredictionResult result = predictionService.predictCompletion(user, plannedPoints, teamCapacity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recommendation")
    public ResponseEntity<String> getRecommendation(
            @RequestParam int plannedPoints,
            @RequestParam int teamCapacity,
            Authentication authentication) {
        User user = getCurrentUser(authentication);
        PredictionResult result = predictionService.predictCompletion(user, plannedPoints, teamCapacity);
        return ResponseEntity.ok(result.getRecommendation());
    }
}
