package com.project.fitnesstracker.dto;

import com.project.fitnesstracker.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Email(message = "Invalid Email")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "firstname is required")
    private String firstName;
    @NotBlank(message = "lastname is required")
    private String lastName;

    private UserRole role;
}
