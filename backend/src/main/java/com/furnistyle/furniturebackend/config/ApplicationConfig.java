package com.furnistyle.furniturebackend.config;


import com.furnistyle.furniturebackend.auditing.ApplicationAuditAware;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Value("${application.admin.default.username}")
    private String username;

    @Value("${application.admin.default.password}")
    private String password;

    @Value("${application.admin.default.address}")
    private String address;

    @Value("${application.admin.default.fullname}")
    private String fullname;

    @Value("${application.admin.default.email}")
    private String email;

    @Value("${application.admin.default.phone}")
    private String phone;

    @Value("${application.admin.default.dob}")
    private LocalDate dateOfBirth;

    @Value("${application.admin.default.gender}")
    private String gender;

    @Bean
    @ConditionalOnProperty(
        prefix = "spring",
        value = "datasource.driver-class-name",
        havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner() {
        log.info("Initializing application.....");
        return args -> {
            if (!userRepository.existsByUsername(username)) {
                User user = User.builder()
                    .username(username)
                    .fullname(fullname)
                    .email(email)
                    .phone(phone)
                    .address(address)
                    .dateOfBirth(dateOfBirth)
                    .gender(gender)
                    .password(passwordEncoder().encode(password))
                    .role("SUPER_ADMIN")
                    .build();
                userRepository.save(user);
                log.warn("Create: admin user has been created: email = {}, password = {} ",
                    username, password);
            } else {
                log.warn("Admin user: email = {}, password = {} ", username, password);
            }
            log.info("Application initialization completed .....");
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new ApplicationAuditAware();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
