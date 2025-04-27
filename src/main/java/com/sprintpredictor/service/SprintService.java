// src/main/java/com/sprintpredictor/service/SprintService.java
package com.sprintpredictor.service;

import com.sprintpredictor.entity.Sprint;
import com.sprintpredictor.entity.SprintTask;
import com.sprintpredictor.entity.User;
import com.sprintpredictor.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    public Sprint createSprint(Sprint sprint, User user) {
        sprint.setUser(user);
        return sprintRepository.save(sprint);
    }

    public List<Sprint> getUserSprints(User user) {
        return sprintRepository.findByUser(user);
    }

    public Sprint getSprintById(Long id, User user) {
        return sprintRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));
    }

    public Sprint updateSprint(Sprint sprint, User user) {
        Sprint existingSprint = getSprintById(sprint.getId(), user);
        existingSprint.setName(sprint.getName());
        existingSprint.setDescription(sprint.getDescription());
        existingSprint.setStartDate(sprint.getStartDate());
        existingSprint.setEndDate(sprint.getEndDate());
        existingSprint.setPlannedCapacity(sprint.getPlannedCapacity());
        return sprintRepository.save(existingSprint);
    }

    public void deleteSprint(Long id, User user) {
        Sprint sprint = getSprintById(id, user);
        sprintRepository.delete(sprint);
    }

    public Sprint addTaskToSprint(Long sprintId, SprintTask task, User user) {
        Sprint sprint = getSprintById(sprintId, user);
        task.setSprint(sprint);
        sprint.getTasks().add(task);
        return sprintRepository.save(sprint);
    }

    public Sprint updateTaskStatus(Long sprintId, Long taskId, String status, User user) {
        Sprint sprint = getSprintById(sprintId, user);
        SprintTask task = sprint.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return sprintRepository.save(sprint);
    }

    public List<Sprint> getSprintsBetweenDates(User user, LocalDate start, LocalDate end) {
        return sprintRepository.findByUserAndStartDateBetween(user, start, end);
    }
}