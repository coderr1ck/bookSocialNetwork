package com.coderrr1ck.bookBackend.controller;

import com.coderrr1ck.bookBackend.authDTOs.AuthRequestHandler;
import com.coderrr1ck.bookBackend.authDTOs.validations.RegisterRequestHandler;
import com.coderrr1ck.bookBackend.service.AuthenticationService;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Validated({Default.class, RegisterRequestHandler.class}) @RequestBody AuthRequestHandler registerRequestData) {
        authenticationService.registerUser(registerRequestData);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Validated @RequestBody AuthRequestHandler loginRequestData) {
        return ResponseEntity.ok("User logged in successfully");
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("User logged out successfully");
    }
}
