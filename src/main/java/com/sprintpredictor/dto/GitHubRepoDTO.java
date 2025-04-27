// src/main/java/com/sprintpredictor/dto/GitHubRepoDTO.java
package com.sprintpredictor.dto;

import lombok.Data;

@Data
public class GitHubRepoDTO {
    private Long id;
    private String name;
    private String full_name;
    private String html_url;
    private String description;
    private String language;
    private Integer stargazers_count;
    private Integer forks_count;
    private Integer open_issues_count;
    private String created_at;
    private String updated_at;
    private String pushed_at;
    private GitHubUserDTO owner;
}
