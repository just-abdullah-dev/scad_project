// src/main/java/com/sprintpredictor/service/PredictionService.java
package com.sprintpredictor.service;

import com.sprintpredictor.entity.Sprint;
import com.sprintpredictor.entity.User;
import com.sprintpredictor.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private VelocityCalculator velocityCalculator;

    @Autowired
    private GitHubService githubService;

    public int predictNextSprintVelocity(User user) {
        List<Sprint> sprints = sprintRepository.findByUser(user);
        if (sprints.isEmpty()) return 20; // default if no history
        return (int) Math.round(velocityCalculator.calculateAverageVelocity(sprints));
    }

    public int predictNextSprintCapacity(User user) {
        List<Sprint> sprints = sprintRepository.findByUser(user);
        if (sprints.isEmpty()) return 30; // default if no history
        return (int) Math.round(velocityCalculator.calculateAverageCapacity(sprints));
    }

    public PredictionResult predictCompletion(User user, int plannedPoints, int teamCapacity) {
        List<Sprint> sprints = sprintRepository.findByUser(user);

        double avgVelocity = velocityCalculator.calculateAverageVelocity(sprints);
        double avgCapacity = velocityCalculator.calculateAverageCapacity(sprints);
        double completionRate = velocityCalculator.calculateCompletionRate(sprints);

        if (sprints.isEmpty() || avgVelocity == 0 || avgCapacity == 0) {
            // Not enough historical data
            return new PredictionResult(
                    0.5,
                    "Limited data available: Using default 50% probability.",
                    plannedPoints,
                    teamCapacity,
                    avgVelocity,
                    avgCapacity
            );
        }

        double velocityProbability = calculateVelocityProbability(plannedPoints, avgVelocity);
        double capacityProbability = calculateCapacityProbability(teamCapacity, avgCapacity);

        // Weighted 70% on velocity, 30% on capacity
        double probability = (0.7 * velocityProbability) + (0.3 * capacityProbability);
        probability = Math.min(0.99, probability); // Cap at 99%
        probability = Math.max(0.01, probability); // Ensure minimum 1%

        return new PredictionResult(
                probability,
                generateRecommendation(probability),
                plannedPoints,
                teamCapacity,
                avgVelocity,
                avgCapacity
        );
    }

    private double calculateVelocityProbability(int plannedPoints, double avgVelocity) {
        if (avgVelocity == 0) return 0.5;
        double ratio = (double) plannedPoints / avgVelocity;
        if (ratio <= 1) return 0.9;
        if (ratio <= 1.2) return 0.75;
        if (ratio <= 1.5) return 0.6;
        return 0.4;
    }

    private double calculateCapacityProbability(int teamCapacity, double avgCapacity) {
        if (avgCapacity == 0) return 0.5;
        double ratio = (double) teamCapacity / avgCapacity;
        if (ratio >= 1) return 0.9;
        if (ratio >= 0.8) return 0.75;
        if (ratio >= 0.6) return 0.6;
        return 0.4;
    }

    private String generateRecommendation(double probability) {
        if (probability > 0.85) {
            return "✅ High confidence: Your sprint goal is achievable!";
        } else if (probability > 0.65) {
            return "⚡ Moderate confidence: Minor adjustments may help.";
        } else if (probability > 0.45) {
            return "⚠️ Low confidence: Consider reducing scope.";
        } else {
            return "❌ Very low confidence: Major replanning needed.";
        }
    }

    // Result DTO
    public static class PredictionResult {
        private final double probability;
        private final String recommendation;
        private final int plannedPoints;
        private final int teamCapacity;
        private final double historicalVelocity;
        private final double historicalCapacity;

        public PredictionResult(double probability, String recommendation,
                                int plannedPoints, int teamCapacity,
                                double historicalVelocity, double historicalCapacity) {
            this.probability = probability;
            this.recommendation = recommendation;
            this.plannedPoints = plannedPoints;
            this.teamCapacity = teamCapacity;
            this.historicalVelocity = historicalVelocity;
            this.historicalCapacity = historicalCapacity;
        }

        // Getters
        public double getProbability() {
            return probability;
        }

        public String getRecommendation() {
            return recommendation;
        }

        public int getPlannedPoints() {
            return plannedPoints;
        }

        public int getTeamCapacity() {
            return teamCapacity;
        }

        public double getHistoricalVelocity() {
            return historicalVelocity;
        }

        public double getHistoricalCapacity() {
            return historicalCapacity;
        }
    }
}
