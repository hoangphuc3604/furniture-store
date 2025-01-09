package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.requests.AuthenticationRequest;
import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.dtos.responses.AuthenticationResponse;
import com.furnistyle.furniturebackend.services.AuthService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (authService.register(request)) {
            return ResponseEntity.ok().body(Constants.Message.REGISTER_SUCCESSFUL);
        } else {
            return ResponseEntity.badRequest().body(Constants.Message.REGISTER_FAILED);
        }
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<Boolean> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendOTPForForgotPassword(email));
    }

    @GetMapping("/verification")
    public ResponseEntity<Boolean> verification(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendOTPForVerification(email));
    }

    @GetMapping("/validateOTP")
    public ResponseEntity<Boolean> validateOTP(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(authService.validateOTP(email, otp));
    }
}