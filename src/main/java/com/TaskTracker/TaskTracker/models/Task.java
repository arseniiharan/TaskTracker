package com.TaskTracker.TaskTracker.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="task_name")
    private String taskName;

    @Column(name="task_start_date")
    private String startDate;

    @Column(name="task_end_date")
    private String endDate;

    @Column(name="task_importance")
    private String taskImportance;

    @ManyToMany
    @JoinTable(
            name = "tasks_teams",
            joinColumns = @JoinColumn(name = "tasks_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;
}
