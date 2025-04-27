// src/main/java/com/sprintpredictor/controller/SprintController.java
package com.sprintpredictor.controller;

import com.sprintpredictor.entity.Role;
import com.sprintpredictor.entity.Sprint;
import com.sprintpredictor.entity.SprintTask;
import com.sprintpredictor.entity.User;
import com.sprintpredictor.repository.RoleRepository;
import com.sprintpredictor.security.services.UserDetailsImpl;
import com.sprintpredictor.service.SprintService;
import com.sprintpredictor.service.VelocityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @Autowired
    private VelocityCalculator velocityCalculator;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public Sprint createSprint(@RequestBody Sprint sprint, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        // Now pass the User entity to the service
        return sprintService.createSprint(sprint, user);

    }

//    @PostMapping
//    public Sprint createSprint(@RequestBody Sprint sprint, Authentication authentication) {

    /// /        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    /// /        User user = userDetails.getUser(); // Assuming your UserDetailsImpl has a method to get the User entity
//        return sprintService.createSprint(sprint, user);
//    }
    @GetMapping
    public List<Sprint> getUserSprints(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        return sprintService.getUserSprints(user);
    }

    @GetMapping("/{id}")
    public Sprint getSprint(@PathVariable Long id, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        return sprintService.getSprintById(id, user);
    }

    @PutMapping("/{id}")
    public Sprint updateSprint(@PathVariable Long id, @RequestBody Sprint sprint,
                               Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        sprint.setId(id);
        return sprintService.updateSprint(sprint, user);
    }

    @DeleteMapping("/{id}")
    public void deleteSprint(@PathVariable Long id, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        sprintService.deleteSprint(id, user);
    }

    @PostMapping("/{sprintId}/tasks")
    public Sprint addTaskToSprint(@PathVariable Long sprintId, @RequestBody SprintTask task,
                                  Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        return sprintService.addTaskToSprint(sprintId, task, user);
    }

    @PatchMapping("/{sprintId}/tasks/{taskId}")
    public Sprint updateTaskStatus(@PathVariable Long sprintId, @PathVariable Long taskId,
                                   @RequestParam String status, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        return sprintService.updateTaskStatus(sprintId, taskId, status, user);
    }

    @GetMapping("/velocity")
    public double getAverageVelocity(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        List<Sprint> sprints = sprintService.getUserSprints(user);
        return velocityCalculator.calculateAverageVelocity(sprints);
    }

    @GetMapping("/completion-rate")
    public double getCompletionRate(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        List<Sprint> sprints = sprintService.getUserSprints(user);
        return velocityCalculator.calculateCompletionRate(sprints);
    }

    @GetMapping("/capacity")
    public int getAverageCapacity(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Create a new User entity based on the UserDetailsImpl
        User user = new User(userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(), userDetails.getGithubUsername());

        List<Sprint> sprints = sprintService.getUserSprints(user);
        return velocityCalculator.calculateAverageCapacity(sprints);
    }
}