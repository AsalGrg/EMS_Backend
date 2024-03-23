package com.backend.configs;

import com.backend.serviceImpls.UserServiceImplementation;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig{

    private static final String[] ADMIN_ROUTES = {
            "/api/v1/admin/**",
            "/admin/**"
    };
    private static final String[] AUTH_ROUTES = {
            "/user",
            "/user/loggedInSnippet",
            "/user/profile"
    };

    private static final String[] PUBLIC_ROUTES = {
            "/register",
            "/verify-otp",
            "/login",
            "/search/{eventTitle}/{eventVenue}",
            "/search/quickSearch/{keyword}",
            "/addFirstPageInfo",
            "/addSecondPageInfo"
    };
    private JwtFilter jwtAuthFilter;

    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Autowired
    public SecurityConfig(
            AuthenticationProvider authenticationProvider,
            JwtFilter jwtAuthFilter
                          ){
        this.authenticationProvider= authenticationProvider;
        this.jwtAuthFilter= jwtAuthFilter;
    }


    public SecurityConfig(
            UserServiceImplementation userServiceImplementation,
            PasswordEncoder passwordEncoder,
            JwtFilter jwtFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ){
        this.jwtAuthenticationEntryPoint= jwtAuthenticationEntryPoint;
        this.jwtAuthFilter= jwtFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(req ->
                        req.dispatcherTypeMatchers(DispatcherType.ASYNC, DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                                .requestMatchers(PUBLIC_ROUTES).permitAll()
                                .requestMatchers(AUTH_ROUTES).authenticated()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception)-> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.build();
    }
}
