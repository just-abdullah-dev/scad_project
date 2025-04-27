package com.sprintpredictor.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String githubUsername;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String githubUsername, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.githubUsername = githubUsername;
        this.email = email;
        this.roles = roles;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public String getGithubUsername() {
        return githubUsername;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
