package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.requests.AuthenticationRequest;
import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.dtos.responses.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    boolean register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    boolean sendOTPForForgotPassword(String email);

    boolean sendOTPForVerification(String email);

    boolean validateOTP(String email, String code);
}
