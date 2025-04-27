package com.sprintpredictor.controller;

import com.sprintpredictor.entity.User;
import com.sprintpredictor.security.services.UserDetailsImpl;
import com.sprintpredictor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public User updateProfile(@RequestBody UpdateProfileRequest updateRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userService.updateUser(
                userDetails.getId(),
                updateRequest.getName(),
                updateRequest.getPassword(),
                updateRequest.getGithubUsername()
        );
    }
    // DTO class inside controller or you can move it out as a separate file

    public static class UpdateProfileRequest {
        private String name;
        private String password;
        private String githubUsername;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getGithubUsername() { return githubUsername; }
        public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }
    }
}
