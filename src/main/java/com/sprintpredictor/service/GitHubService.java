// src/main/java/com/sprintpredictor/service/GitHubService.java
package com.sprintpredictor.service;

import com.sprintpredictor.dto.GitHubRepoDTO;
import com.sprintpredictor.dto.GitHubCommitDTO;
import com.sprintpredictor.dto.GitHubIssueDTO;
import com.sprintpredictor.dto.GitHubPullRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GitHubService {
    @Value("${github.api.base-url}")
    private String githubApiUrl;

    @Value("${github.api.token}")
    private String githubToken;

    @Autowired
    private RestTemplate restTemplate;

    public List<GitHubRepoDTO> getUserRepositories(String username) {
        String url = githubApiUrl + "/users/" + username + "/repos";
        HttpEntity<String> entity = createGitHubHttpEntity(githubToken);
        ResponseEntity<GitHubRepoDTO[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, GitHubRepoDTO[].class);

        return Arrays.asList(response.getBody());
    }

    private HttpEntity<String> createGitHubHttpEntity(String githubToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token "+githubToken);
        return new HttpEntity<>(headers);
    }


    public List<GitHubCommitDTO> getRepositoryCommits(String owner, String repo) {
        String url = githubApiUrl + "/repos/" + owner + "/" + repo + "/commits";
        HttpEntity<String> entity = createGitHubHttpEntity();
        
        ResponseEntity<GitHubCommitDTO[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, GitHubCommitDTO[].class);
        
        return Arrays.asList(response.getBody());
    }

    public List<GitHubIssueDTO> getRepositoryIssues(String owner, String repo) {
        String url = githubApiUrl + "/repos/" + owner + "/" + repo + "/issues?state=all";
        HttpEntity<String> entity = createGitHubHttpEntity();
        
        ResponseEntity<GitHubIssueDTO[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, GitHubIssueDTO[].class);
        
        return Arrays.asList(response.getBody());
    }

    public List<GitHubPullRequestDTO> getRepositoryPullRequests(String owner, String repo) {
        String url = githubApiUrl + "/repos/" + owner + "/" + repo + "/pulls?state=all";
        HttpEntity<String> entity = createGitHubHttpEntity();
        
        ResponseEntity<GitHubPullRequestDTO[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, GitHubPullRequestDTO[].class);
        
        return Arrays.asList(response.getBody());
    }

    private HttpEntity<String> createGitHubHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + githubToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        return new HttpEntity<>(headers);
    }
}