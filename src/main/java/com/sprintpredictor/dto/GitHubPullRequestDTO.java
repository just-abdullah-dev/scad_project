// src/main/java/com/sprintpredictor/dto/GitHubPullRequestDTO.java
package com.sprintpredictor.dto;

import lombok.Data;

@Data
public class GitHubPullRequestDTO {
    private Long id;
    private String title;
    private String state;
    private String created_at;
    private String updated_at;
    private String closed_at;
    private String merged_at;
    private String body;
    private GitHubUserDTO user;
    private GitHubUserDTO assignee;
    private Integer comments;
    private Integer commits;
    private Integer additions;
    private Integer deletions;
    private Integer changed_files;
    private String html_url;
}