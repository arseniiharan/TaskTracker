package com.TaskTracker.TaskTracker.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_start_date")
    private String startDate;

    @Column(name = "task_end_date")
    private String endDate;

    @Column(name = "task_importance")
    private String taskImportance;

    @ManyToMany(mappedBy = "tasks")
    private List<Team> teams;
}
