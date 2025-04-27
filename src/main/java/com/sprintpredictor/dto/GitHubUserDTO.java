package com.sprintpredictor.dto;

import lombok.Data;

@Data
public class GitHubUserDTO {
    private String login;
    private Long id;
    private String avatar_url;
    private String html_url;
    private String type;
}