// src/main/java/com/sprintpredictor/service/VelocityCalculator.java
package com.sprintpredictor.service;

import com.sprintpredictor.entity.Sprint;
import com.sprintpredictor.entity.SprintTask;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VelocityCalculator {
    public double calculateAverageVelocity(List<Sprint> sprints) {
        List<Sprint> completedSprints = sprints.stream()
                .filter(s -> s.getCompletedPoints() != null)
                .collect(Collectors.toList());
        
        if (completedSprints.isEmpty()) {
            return 0;
        }
        
        return completedSprints.stream()
                .mapToInt(Sprint::getCompletedPoints)
                .average()
                .orElse(0);
    }

    public double calculateCompletionRate(List<Sprint> sprints) {
        long completedTasks = sprints.stream()
                .flatMap(s -> s.getTasks().stream())
                .filter(t -> "Done".equalsIgnoreCase(t.getStatus()))
                .count();
        
        long totalTasks = sprints.stream()
                .flatMap(s -> s.getTasks().stream())
                .count();
        
        if (totalTasks == 0) {
            return 0;
        }
        
        return (double) completedTasks / totalTasks * 100;
    }

    public int calculateTotalCompletedPoints(List<Sprint> sprints) {
        return sprints.stream()
                .filter(s -> s.getCompletedPoints() != null)
                .mapToInt(Sprint::getCompletedPoints)
                .sum();
    }

    public int calculateAverageCapacity(List<Sprint> sprints) {
        List<Sprint> completedSprints = sprints.stream()
                .filter(s -> s.getActualCapacity() != null)
                .collect(Collectors.toList());
        
        if (completedSprints.isEmpty()) {
            return 0;
        }
        
        return (int) completedSprints.stream()
                .mapToInt(Sprint::getActualCapacity)
                .average()
                .orElse(0);
    }
}