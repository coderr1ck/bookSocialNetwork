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
        return authenticationService.registerUser(registerRequestData);
//        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Validated({Default.class}) @RequestBody AuthRequestHandler loginRequestData) {
        return authenticationService.loginUser(loginRequestData);
//        return ResponseEntity.ok("User logged in successfully");
    }

    @GetMapping("activate-account/{code}")
//    redirect to front-end url from there we will make bacckend api call
    public ResponseEntity<?> activateAccount(
                   @PathVariable String code
    ) {
        return authenticationService.activateAccount(code);
    }
}
