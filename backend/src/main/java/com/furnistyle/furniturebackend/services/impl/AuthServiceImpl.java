package com.furnistyle.furniturebackend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnistyle.furniturebackend.dtos.requests.AuthenticationRequest;
import com.furnistyle.furniturebackend.dtos.requests.RegisterRequest;
import com.furnistyle.furniturebackend.dtos.responses.AuthenticationResponse;
import com.furnistyle.furniturebackend.enums.EGender;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.ETypeOfOTP;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.models.OTP;
import com.furnistyle.furniturebackend.models.Token;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.OTPRepository;
import com.furnistyle.furniturebackend.repositories.TokenRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.AuthService;
import com.furnistyle.furniturebackend.services.JwtService;
import com.furnistyle.furniturebackend.services.MailService;
import com.furnistyle.furniturebackend.services.UserService;
import com.furnistyle.furniturebackend.utils.Constants;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailService mailService;
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final OTPRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${application.otp.time}")
    int second;

    @Override
    public boolean register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new BadRequestException(Constants.Message.ALREADY_USERNAME_REGISTER);
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new BadRequestException(Constants.Message.ALREADY_MAIL_REGISTER);
        }

        if (userRepository.findByPhone(request.getPhone()) != null) {
            throw new BadRequestException(Constants.Message.ALREADY_PHONE_REGISTER);
        }

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
            throw new BadRequestException(Constants.Message.INCORRECT_USERNAME_OR_PASS);
        }
        var user = repository.findByUsername(request.getUsername());
        if (user == null) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_USER);
        }

        boolean isDisable = user.getStatus() != EUserStatus.ACTIVE;
        if (isDisable) {
            throw new BadRequestException(Constants.Message.BLOCKED_ACCOUNT);
        }

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
    }

    @Override
    public boolean sendOTPForForgotPassword(String email) {
        return sendOTP(email, ETypeOfOTP.FORGOT);
    }

    @Override
    public boolean sendOTPForVerification(String email) {
        return sendOTP(email, ETypeOfOTP.VERIFY);
    }

    @Override
    public boolean validateOTP(String email, String code) {
        OTP otp = otpRepository.getByEmail(email);
        if (otp == null) {
            throw new BadRequestException(Constants.Message.NOT_MATCH_CODE);
        }

        if (!Objects.equals(otp.getCode(), code)) {
            throw new BadRequestException(Constants.Message.NOT_MATCH_CODE);
        }

        if (otp.getCreatedDate().plusMinutes(second).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(Constants.Message.EXPIRED_CODE);
        }

        if (otp.getType() == ETypeOfOTP.FORGOT) {
            userService.resetPassword(email);
        }

        otpRepository.delete(otp);
        return true;
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

    private String geneBodyForMail(String fullname, String otp, ETypeOfOTP type) {
        if (Objects.equals(type, ETypeOfOTP.FORGOT)) {
            return """
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Quên mật khẩu</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            margin: 0;
                            padding: 0;
                            background-color: #f9f9f9;
                            color: #333;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #fff;
                            border: 1px solid #ddd;
                            border-radius: 5px;
                            overflow: hidden;
                            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            background-color: #FF5722;
                            color: #fff;
                            text-align: center;
                            padding: 15px;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .content p {
                            margin: 10px 0;
                        }
                        .otp {
                            display: block;
                            font-size: 20px;
                            font-weight: bold;
                            color: #FF5722;
                            text-align: center;
                            margin: 20px 0;
                        }
                        .footer {
                            text-align: center;
                            padding: 10px;
                            background-color: #f1f1f1;
                            font-size: 14px;
                            color: #777;
                        }
                        .footer a {
                            color: #FF5722;
                            text-decoration: none;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <h1>QUÊN MẬT KHẨU</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào""" + " " + fullname + """
                ,</p>
                <p>Bạn vừa yêu cầu khôi phục mật khẩu cho tài khoản của mình.\s
                Đây là mã xác thực (OTP) của bạn:</p>
                <div class="otp">""" + otp + """
                            </div>
                            <p>Vui lòng nhập mã này vào trang khôi phục mật khẩu để tiếp tục.</p>
                            <p>Nếu bạn không yêu cầu thay đổi mật khẩu, xin vui lòng bỏ qua email này.</p>
                            <p>Trân trọng,</p>
                            <p><strong>Đội ngũ Hỗ trợ</strong></p>
                        </div>
                        <div class="footer">
                            <p>Bạn nhận được email này vì bạn đã\s
                            đăng ký tài khoản tại hệ thống của chúng tôi.</p>
                            <p><a href="https://yourwebsite.com">Truy cập website</a> để biết thêm thông tin.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        }

        if (Objects.equals(type, ETypeOfOTP.VERIFY)) {
            return """
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác Thực Đăng Ký</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            margin: 0;
                            padding: 0;
                            background-color: #f9f9f9;
                            color: #333;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #fff;
                            border: 1px solid #ddd;
                            border-radius: 5px;
                            overflow: hidden;
                            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            background-color: #4CAF50;
                            color: #fff;
                            text-align: center;
                            padding: 15px;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .content p {
                            margin: 10px 0;
                        }
                        .otp {
                            display: block;
                            font-size: 20px;
                            font-weight: bold;
                            color: #4CAF50;
                            text-align: center;
                            margin: 20px 0;
                        }
                        .footer {
                            text-align: center;
                            padding: 10px;
                            background-color: #f1f1f1;
                            font-size: 14px;
                            color: #777;
                        }
                        .footer a {
                            color: #4CAF50;
                            text-decoration: none;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <h1>XÁC THỰC TÀI KHOẢN</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào""" + " " + fullname + """
                ,</p>
                <p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống của chúng tôi.\s
                Để hoàn tất việc đăng ký, vui lòng sử dụng mã xác thực (OTP) dưới đây:</p>
                <div class="otp">""" + otp + """
                            </div>
                            <p>Hãy nhập mã này trên trang xác thực tài khoản để kích hoạt tài khoản của bạn.</p>
                            <p>Nếu bạn không yêu cầu đăng ký tài khoản, xin vui lòng bỏ qua email này.</p>
                            <p>Trân trọng,</p>
                            <p><strong>Đội ngũ Hỗ trợ</strong></p>
                        </div>
                        <div class="footer">
                            <p>Bạn nhận được email này vì bạn đã sử dụng\s
                            địa chỉ email này để đăng ký tài khoản trên hệ thống của chúng tôi.</p>
                            <p><a href="https://yourwebsite.com">Truy cập website</a> để biết thêm thông tin.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

        }

        return null;
    }

    private boolean sendOTP(String email, ETypeOfOTP type) {
        GoogleAuthenticator gauth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gauth.createCredentials();
        String secret = key.getKey();

        int otp = gauth.getTotpPassword(secret);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_USER);
        }

        String body = geneBodyForMail(user.getFullname(), String.valueOf(otp), type);

        String subject = ((Objects.equals(type, ETypeOfOTP.VERIFY)) ? "XÁC THỰC TÀI KHOẢN" : "QUÊN MẬT KHẨU");
        mailService.sendEmail(email, subject, body);
        OTP check = otpRepository.getByEmail(email);
        if (check != null) {
            otpRepository.delete(check);
        }

        OTP entity = new OTP();
        entity.setCode(String.valueOf(otp));
        entity.setEmail(email);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setType(type);
        otpRepository.save(entity);

        return true;
    }
}