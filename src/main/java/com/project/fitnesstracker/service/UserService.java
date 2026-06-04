package com.project.fitnesstracker.service;

import com.project.fitnesstracker.dto.LoginRequest;
import com.project.fitnesstracker.dto.RegisterRequest;
import com.project.fitnesstracker.dto.UserResponce;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.entity.UserRole;
import com.project.fitnesstracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponce register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        UserRole role = registerRequest.getRole() != null ? registerRequest.getRole() : UserRole.USER;
        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        return maptoResponce(userRepository.save(user));
    }

    public UserResponce maptoResponce(User savedUser) {
        UserResponce userResponce = new UserResponce();
        userResponce.setId(savedUser.getId());
        userResponce.setEmail(savedUser.getEmail());
        userResponce.setRole(savedUser.getRole());
        userResponce.setFirstName(savedUser.getFirstName());
        userResponce.setLastName(savedUser.getLastName());
        userResponce.setCreatedAt(savedUser.getCreatedAt());
        userResponce.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponce;
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) throw new RuntimeException("Invalid email");
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public UserResponce getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return maptoResponce(user);
    }

    public UserResponce updateUser(Long id, RegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        return maptoResponce(userRepository.save(user));
    }

    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<UserResponce> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::maptoResponce)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalAdmins", userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == UserRole.ADMIN)
                .count());
        return stats;
    }
}