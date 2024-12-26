package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.requests.AuthenticationRequest;
import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.dtos.responses.AuthenticationResponse;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
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
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterRequest request
    ) {
        if (authService.register(request)) {
            return ResponseEntity.ok().body("Register successfully!");
        } else {
            return ResponseEntity.badRequest().body("Register failed!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Deploy successfully!");
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok("CI/CD thành công rồi mấy đứa ơi :)))");
    }

    @PostMapping("/getUser")
    public ResponseEntity<Optional<User>> getUser(@RequestBody Map<String, String> username) {
        return ResponseEntity.ok(userRepository.findByUsername(username.get("username")));
    }
}