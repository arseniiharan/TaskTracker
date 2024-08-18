package com.TaskTracker.TaskTracker.repositories;

import com.TaskTracker.TaskTracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskName(String taskName);
    @Query("SELECT k FROM Task k JOIN k.teams t WHERE t.id = :teamId")
    List<Task> getAllTasksWithinTeam(@Param("teamId") UUID teamId);

    Optional<Task> findById(UUID taskId);
}
