package com.TaskTracker.TaskTracker.repositories;

import com.TaskTracker.TaskTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.teams t WHERE t.id = :teamId")
    List<User> getAllUsersWithTeam(@Param("teamId") UUID teamId);
    Optional<User> findByActivationCode(String activationCode);
}
