// src/main/java/com/sprintpredictor/entity/Sprint.java
package com.sprintpredictor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sprints")
@Data
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String goal;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "planned_capacity")
    private Integer plannedCapacity;
    
    @Column(name = "actual_capacity")
    private Integer actualCapacity;
    
    @Column(name = "completed_points")
    private Integer completedPoints;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "githubToken"})
    private User user;
    
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SprintTask> tasks;
}