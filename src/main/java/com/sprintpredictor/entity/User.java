// src/main/java/com/sprintpredictor/entity/User.java
package com.sprintpredictor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    
    private String name;
    
    @Column(name = "provider")
    private String provider; // "local", "google", "github"
    
    @Column(name = "provider_id")
    private String providerId;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    @Column(name = "github_token")
    private String githubToken;
    
    @Column(name = "github_username")
    private String githubUsername;

    public User(Long id, String email, String name, String githubUsername) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.githubUsername = githubUsername;
    }

    public User() {

    }

    public String getGithubUsername() {
        return githubUsername;
    }
    public User(String email, String password, String name, String provider, String providerId, String imageUrl, Set<Role> roles, String githubToken, String githubUsername) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.roles = roles;
        this.githubToken = githubToken;
        this.githubUsername = githubUsername;
    }
}