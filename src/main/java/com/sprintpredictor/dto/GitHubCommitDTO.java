package com.sprintpredictor.dto;

import lombok.Data;

@Data
public class GitHubCommitDTO {
    private String sha;
    private Commit commit;
    private String html_url;
    private GitHubUserDTO author;
    private GitHubUserDTO committer;

    @Data
    public static class Commit {
        private Author author;
        private Author committer;
        private String message;
        
        @Data
        public static class Author {
            private String name;
            private String email;
            private String date;
        }
    }
}