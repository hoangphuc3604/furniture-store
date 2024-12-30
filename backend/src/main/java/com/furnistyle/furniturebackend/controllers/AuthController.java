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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (authService.register(request)) {
            return ResponseEntity.ok().body(Constants.Message.REGISTER_SUCCESSFUL);
        } else {
            return ResponseEntity.badRequest().body(Constants.Message.REGISTER_FAILED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Deploy successfully!");
    }

    @GetMapping("/test3")
    public ResponseEntity<String> test3() {
        return ResponseEntity.ok("CI/CD thành công rồi mấy đứa ơi :)))");
    }
}