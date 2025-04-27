// src/main/java/com/sprintpredictor/controller/GitHubController.java
package com.sprintpredictor.controller;

import java.util.Map;

import com.sprintpredictor.dto.*;
import com.sprintpredictor.security.services.UserDetailsImpl;
import com.sprintpredictor.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {
    @Autowired
    private GitHubService githubService;

//    @GetMapping("/repos")
//    public List<GitHubRepoDTO> getUserRepositories(Authentication authentication) {
//        // Get username from authentication
//        System.out.println(authentication);
//        String username = authentication.getName();
//        return githubService.getUserRepositories(username);
//    }

    @GetMapping("/repos")
    public List<GitHubRepoDTO> getUserRepositories(Authentication authentication) {
        // Check if the principal is an instance of UserDetailsImpl
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String githubUsername = userDetails.getGithubUsername();
            System.out.println(githubUsername);
            if (githubUsername == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please add your GitHub username.");
            }

            // Use the githubUsername to call the service
            return githubService.getUserRepositories(githubUsername);
        } else {
            throw new RuntimeException("Principal is not of type UserDetailsImpl");
        }
    }

    @GetMapping("/repos/{owner}/{repo}/commits")
    public List<GitHubCommitDTO> getRepositoryCommits(
            @PathVariable String owner,
            @PathVariable String repo) {
        return githubService.getRepositoryCommits(owner, repo);
    }

    @GetMapping("/repos/{owner}/{repo}/issues")
    public List<GitHubIssueDTO> getRepositoryIssues(
            @PathVariable String owner,
            @PathVariable String repo) {
        return githubService.getRepositoryIssues(owner, repo);
    }

    @GetMapping("/repos/{owner}/{repo}/pulls")
    public List<GitHubPullRequestDTO> getRepositoryPullRequests(
            @PathVariable String owner,
            @PathVariable String repo) {
        return githubService.getRepositoryPullRequests(owner, repo);
    }
}