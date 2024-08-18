package com.TaskTracker.TaskTracker.repositories;

import com.TaskTracker.TaskTracker.DTO.teams.TeamDTO;
import com.TaskTracker.TaskTracker.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);

    Optional<Team> findById(UUID teamId);
}
