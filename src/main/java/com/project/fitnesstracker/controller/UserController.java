package com.project.fitnesstracker.controller;

import com.project.fitnesstracker.dto.LoginRequest;
import com.project.fitnesstracker.dto.LoginResponce;
import com.project.fitnesstracker.dto.RegisterRequest;
import com.project.fitnesstracker.dto.UserResponce;
import com.project.fitnesstracker.entity.User;
import com.project.fitnesstracker.repositories.UserRepository;
import com.project.fitnesstracker.security.JwtUtils;
import com.project.fitnesstracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserResponce> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponce> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest);
            String token = jwtUtils.genrateToken(user.getId(), user.getRole().name());
            return ResponseEntity.ok(new LoginResponce(token, userService.maptoResponce(user)));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponce> getProfile(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponce> updateProfile(
            Authentication authentication,
            @RequestBody RegisterRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody LoginRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        userService.changePassword(userId, request.getPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}