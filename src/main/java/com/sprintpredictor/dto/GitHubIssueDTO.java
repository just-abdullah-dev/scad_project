package com.sprintpredictor.dto;

import lombok.Data;

@Data
public class GitHubIssueDTO {
    private Long id;
    private String title;
    private String state;
    private String created_at;
    private String updated_at;
    private String closed_at;
    private String body;
    private GitHubUserDTO user;
    private GitHubUserDTO assignee;
    private Integer comments;
    private String html_url;
}