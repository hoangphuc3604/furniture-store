package com.furnistyle.furniturebackend.config.security;


import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.filters.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {"/auth/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.addAllowedOriginPattern("*");
                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }))
            .authorizeHttpRequests(req -> req
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers("/user/*")
                .hasAnyRole(String.valueOf(ERole.USER), String.valueOf(ERole.ADMIN),
                    String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/user/admin/*")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/user/superAdmin/*").hasRole(String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/superadmin/*").hasRole(String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/categories/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/materials/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/medias/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/products/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers(HttpMethod.PUT, "/products/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers(HttpMethod.DELETE, "/products/**")
                .hasAnyRole(String.valueOf(ERole.ADMIN), String.valueOf(ERole.SUPER_ADMIN))
                .requestMatchers("/order/*").authenticated()
                .requestMatchers("/order/admin/*").hasAnyRole(String.valueOf(ERole.SUPER_ADMIN), String.valueOf(ERole.ADMIN))
                .requestMatchers("/cart/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/reviews/**").authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication)
                    -> SecurityContextHolder.clearContext())
            );

        return http.build();
    }

}
