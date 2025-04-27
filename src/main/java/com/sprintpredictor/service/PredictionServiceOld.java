//// src/main/java/com/sprintpredictor/service/PredictionService.java
//package com.sprintpredictor.service;
//
//import com.sprintpredictor.entity.Sprint;
//import com.sprintpredictor.entity.User;
//import com.sprintpredictor.repository.SprintRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PredictionServiceOld {
//    @Autowired
//    private SprintRepository sprintRepository;
//
//    @Autowired
//    private VelocityCalculator velocityCalculator;
//
//    @Autowired
//    private GitHubService githubService;
//
//    public int predictNextSprintVelocity(User user) {
//        List<Sprint> sprints = sprintRepository.findByUser(user);
//        return (int) Math.round(velocityCalculator.calculateAverageVelocity(sprints));
//    }
//
//    public int predictNextSprintCapacity(User user) {
//        List<Sprint> sprints = sprintRepository.findByUser(user);
//        return velocityCalculator.calculateAverageCapacity(sprints);
//    }
//
//    public PredictionResult predictCompletion(User user, int plannedPoints, int teamCapacity) {
//        List<Sprint> sprints = sprintRepository.findByUser(user);
//
//        // Calculate key metrics
//        double avgVelocity = velocityCalculator.calculateAverageVelocity(sprints);
//        double avgCapacity = velocityCalculator.calculateAverageCapacity(sprints);
//        double completionRate = velocityCalculator.calculateCompletionRate(sprints);
//
//        // Handle case with no historical data
//        if (sprints.isEmpty()) {
//            return new PredictionResult(
//                    0.5,
//                    "No historical data available - using default prediction",
//                    plannedPoints,
//                    teamCapacity,
//                    0,
//                    0
//            );
//        }
//
//        // Calculate probability considering both velocity and capacity
//        double velocityProbability = calculateVelocityProbability(plannedPoints, avgVelocity);
//        double capacityProbability = calculateCapacityProbability(teamCapacity, avgCapacity);
//
//        // Weighted probability (60% velocity, 40% capacity)
//        double probability = (0.6 * velocityProbability) + (0.4 * capacityProbability);
//        probability = Math.min(0.99, probability); // Cap at 99%
//
//        return new PredictionResult(
//                probability,
//                generateRecommendation(probability),
//                plannedPoints,
//                teamCapacity,
//                avgVelocity,
//                avgCapacity
//        );
//    }
//
//    private double calculateVelocityProbability(int plannedPoints, double avgVelocity) {
//        if (avgVelocity == 0) return 0;
//        double ratio = plannedPoints / avgVelocity;
//        return 1 / Math.max(ratio, 1);
//    }
//
//    private double calculateCapacityProbability(int teamCapacity, double avgCapacity) {
//        if (avgCapacity == 0) return 0;
//        double ratio = teamCapacity / avgCapacity;
//        return 1 / Math.max(ratio, 1);
//    }
//
//    private String generateRecommendation(double probability) {
//        if (probability > 0.8) {
//            return "High confidence: Team can complete all points with current capacity";
//        } else if (probability > 0.6) {
//            return "Moderate confidence: Consider minor scope adjustments";
//        } else if (probability > 0.4) {
//            return "Low confidence: Reduce scope by 20-30% or extend timeline";
//        } else {
//            return "Very high risk: Needs significant replanning (40%+ scope reduction)";
//        }
//    }
//
//    // Result DTO
//    public static class PredictionResult {
//        private final double probability;
//        private final String recommendation;
//        private final int plannedPoints;
//        private final int teamCapacity;
//        private final double historicalVelocity;
//        private final double historicalCapacity;
//
//        public PredictionResult(double probability, String recommendation,
//                                int plannedPoints, int teamCapacity,
//                                double historicalVelocity, double historicalCapacity) {
//            this.probability = probability;
//            this.recommendation = recommendation;
//            this.plannedPoints = plannedPoints;
//            this.teamCapacity = teamCapacity;
//            this.historicalVelocity = historicalVelocity;
//            this.historicalCapacity = historicalCapacity;
//        }
//
//        // Getters
//        public double getProbability() {
//            return probability;
//        }
//
//        public String getRecommendation() {
//            return recommendation;
//        }
//
//        public int getPlannedPoints() {
//            return plannedPoints;
//        }
//
//        public int getTeamCapacity() {
//            return teamCapacity;
//        }
//
//        public double getHistoricalVelocity() {
//            return historicalVelocity;
//        }
//
//        public double getHistoricalCapacity() {
//            return historicalCapacity;
//        }
//    }
//}
////// src/main/java/com/sprintpredictor/service/PredictionService.java
////package com.sprintpredictor.service;
////
////import com.sprintpredictor.entity.Sprint;
////import com.sprintpredictor.entity.User;
////import com.sprintpredictor.repository.SprintRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.util.List;
////
////@Service
////public class PredictionService {
////    @Autowired
////    private SprintRepository sprintRepository;
////
////    @Autowired
////    private VelocityCalculator velocityCalculator;
////
////    @Autowired
////    private GitHubService githubService;
////
////    public int predictNextSprintVelocity(User user) {
////        List<Sprint> sprints = sprintRepository.findByUser(user);
////        return (int) Math.round(velocityCalculator.calculateAverageVelocity(sprints));
////    }
////
////    public int predictNextSprintCapacity(User user) {
////        List<Sprint> sprints = sprintRepository.findByUser(user);
////        return velocityCalculator.calculateAverageCapacity(sprints);
////    }
////
////    public double predictCompletionProbability(User user, int plannedPoints) {
////        List<Sprint> sprints = sprintRepository.findByUser(user);
////        double avgVelocity = velocityCalculator.calculateAverageVelocity(sprints);
////        double completionRate = velocityCalculator.calculateCompletionRate(sprints);
////
////        if (avgVelocity == 0) {
////            return 0;
////        }
////
////        // Simple prediction model - can be enhanced with more sophisticated algorithms
////        double velocityRatio = plannedPoints / avgVelocity;
////        double probability = completionRate / 100 * (1 / Math.max(velocityRatio, 1));
////
////        return Math.min(probability, 1.0);
////    }
////
////    public String getRecommendation(User user, int plannedPoints) {
////        double probability = predictCompletionProbability(user, plannedPoints);
////
////        if (probability > 0.8) {
////            return "High confidence: The team can likely complete all planned points.";
////        } else if (probability > 0.6) {
////            return "Moderate confidence: The team may complete most planned points.";
////        } else if (probability > 0.4) {
////            return "Low confidence: Consider reducing scope or extending timeline.";
////        } else {
////            return "Very low confidence: Significant scope reduction needed.";
////        }
////    }
////}