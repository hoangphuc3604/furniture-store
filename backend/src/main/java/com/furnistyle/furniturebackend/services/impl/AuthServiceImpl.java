package com.furnistyle.furniturebackend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnistyle.furniturebackend.dtos.requests.AuthenticationRequest;
import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.dtos.responses.AuthenticationResponse;
import com.furnistyle.furniturebackend.enums.EGender;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.models.Token;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.TokenRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.AuthService;
import com.furnistyle.furniturebackend.services.JwtService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean register(RegisterRequest request) {
        var user = User.builder()
            .username(request.getUsername())
            .fullname(request.getFullname())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .dateOfBirth(request.getDateOfBirth())
            .gender(EGender.valueOf(request.getGender()))
            .password(passwordEncoder.encode(request.getPassword()))
            .role(ERole.valueOf((request.getRole() == null ? "USER" : request.getRole())))
            .status(EUserStatus.ACTIVE)
            .build();

        return repository.save(user).getId() > 0;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(Constants.Message.INCORRECT_USERNAME_OR_PASSWORD_MESSAGE);
        }
        var user = repository.findByUsername(request.getUsername());
        if (user == null) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_USER);
        }

        boolean isDisable = user.getStatus() != EUserStatus.ACTIVE;
        if (isDisable) {
            throw new BadRequestException(Constants.Message.BLOCKED_ACOUNT);
        }

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByUsername(username);
            if (user == null) {
                throw new NotFoundException(Constants.Message.NOT_FOUND_USER);
            }
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
