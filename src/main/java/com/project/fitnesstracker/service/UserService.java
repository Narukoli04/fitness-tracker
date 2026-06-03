package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.LoginRequest;
import com.project.fitnesstracker.dto.RegisterRequest;
import com.project.fitnesstracker.dto.UserResponce;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.entity.UserRole;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponce register(RegisterRequest registerRequest) {
        UserRole role = registerRequest.getRole()!=null?registerRequest.getRole():UserRole.USER;
    User user=User.builder()
            .email(registerRequest.getEmail())
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .role(role)
            .build();
//        User user = new User(
//                0,
//                registerRequest.getEmail(),
//                registerRequest.getPassword(),
//                registerRequest.getFirstName(),
//                registerRequest.getLastName(),
//
//                Instant.parse("2026-12-08T14:49:41.208Z")
//                        .atZone(ZoneOffset.UTC)
//                        .toLocalDateTime(),
//
//                Instant.parse("2026-12-08T14:49:41.208Z")
//                        .atZone(ZoneOffset.UTC)
//                        .toLocalDateTime(),
//
//                List.of(),
//                List.of()
//        );

        User savedUser = userRepository.save(user);



        return maptoResponce(savedUser);
    }

    public UserResponce maptoResponce(User savedUser) {
        UserResponce userResponce = new UserResponce();
        userResponce.setId(savedUser.getId());
        userResponce.setEmail(savedUser.getEmail());
        userResponce.setFirstName(savedUser.getFirstName());
        userResponce.setLastName(savedUser.getLastName());
        userResponce.setCreatedAt(savedUser.getCreatedAt());
        userResponce.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponce;
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) throw new RuntimeException("Invalid email ");

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid  password");
        }
        return user;
    }

}

