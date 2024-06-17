package com.TaskTracker.TaskTracker.services.users.impl;

import com.TaskTracker.TaskTracker.DTO.users.UserRegistrationRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserAlreadyExistsException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.mappers.UserMapper;
import com.TaskTracker.TaskTracker.models.Role;
import com.TaskTracker.TaskTracker.models.Team;
import com.TaskTracker.TaskTracker.models.User;
import com.TaskTracker.TaskTracker.repositories.RoleRepository;
import com.TaskTracker.TaskTracker.repositories.TeamRepository;
import com.TaskTracker.TaskTracker.repositories.UserRepository;
import com.TaskTracker.TaskTracker.services.mails.MailService;
import com.TaskTracker.TaskTracker.services.users.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRegistrationRequestDTO userRegistrationRequestDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userRegistrationRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email like this already exists");
        }

        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDTO.password());
        UserRegistrationRequestDTO encodedPassRegistrationReq = new UserRegistrationRequestDTO(
                encodedPassword,
                userRegistrationRequestDTO.email(),
                userRegistrationRequestDTO.name(),
                userRegistrationRequestDTO.surname(),
                userRegistrationRequestDTO.jobTitle(),
                userRegistrationRequestDTO.teamLeader()
        );

        User user = userMapper.toModel(encodedPassRegistrationReq);
        Role role = roleRepository.findByRoleName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        String message = String.format(
                "Greetings, %s. \n"
                            + "Welcome to Taskonauts. Please, visit the next link to activate your email: \n"
                            + "http://localhost:8080/activate/%s",
                user.getName(),
                user.getActivationCode());

                mailService.sendActiveMail(user.getEmail(), "Activation code", message);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void activateUser(String activationCode) throws UserNeverExistedException {
        User user = userRepository.findByActivationCode(activationCode)
                                                .orElseThrow(() -> new UserNeverExistedException("User doesn't exist"));

        user.setActivationCode(null);
        userRepository.save(user);
    }
    @Override
    @Transactional
    public UserResponseDTO userFindByEmail(String email) throws UserNeverExistedException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("User with this email doesn't exist"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public List<UserResponseDTO> usersFindByTeam(String teamName) throws TeamNeverExistedException {
        Optional<Team> teamOptional = teamRepository.findByTeamName(teamName);
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        List<User> usersWithTeam = userRepository.getAllUsersWithTeam(team.getId());
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

        for (User u : usersWithTeam) {
             userResponseDTOList.add(userMapper.toDto(u));
        }
        return userResponseDTOList;
    }
}
