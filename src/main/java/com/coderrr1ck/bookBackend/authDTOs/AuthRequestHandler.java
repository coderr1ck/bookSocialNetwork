package com.coderrr1ck.bookBackend.authDTOs;


import com.coderrr1ck.bookBackend.authDTOs.validations.RegisterRequestHandler;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthRequestHandler {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character, and be at least 8 characters long"
    )
    private String password;

    @NotBlank(groups = RegisterRequestHandler.class, message = "First name is required")
    private String firstName;

    @NotBlank(groups = RegisterRequestHandler.class, message = "Last name is required")
    private String lastName;

    @NotBlank(groups = RegisterRequestHandler.class, message = "Date of Birth is required")
    private String dob;

    @NotBlank(groups = RegisterRequestHandler.class, message = "Confirm password is required")
    private String confirmPassword;

    @AssertTrue(groups = RegisterRequestHandler.class,message = "Password & Confirm Password didn't match")
    public boolean isPasswordsMatch() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

}
