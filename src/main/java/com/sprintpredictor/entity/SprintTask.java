// src/main/java/com/sprintpredictor/entity/SprintTask.java
package com.sprintpredictor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "sprint_tasks")
@Data
public class SprintTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private Integer points;
    private String status;
    
    @Column(name = "github_issue_id")
    private Long githubIssueId;
    
    @Column(name = "github_pr_id")
    private Long githubPrId;
    
//    @ManyToOne
//    @JoinColumn(name = "sprint_id")
//    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @JsonBackReference
    private Sprint sprint;

}