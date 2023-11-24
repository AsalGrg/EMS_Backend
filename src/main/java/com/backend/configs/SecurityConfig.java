package com.backend.configs;

import com.backend.serviceImpls.UserServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity


public class SecurityConfig{

    private final UserServiceImplementation userServiceImplementation;

    private final PasswordEncoder passwordEncoder;

    private final JwtFilter jwtAuthFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(
            UserServiceImplementation userServiceImplementation,
            PasswordEncoder passwordEncoder,
            JwtFilter jwtFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ){
        this.jwtAuthenticationEntryPoint= jwtAuthenticationEntryPoint;
        this.jwtAuthFilter= jwtFilter;
        this.userServiceImplementation= userServiceImplementation;
        this.passwordEncoder= passwordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {

        https.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttpRequest-> authHttpRequest.requestMatchers("/register").permitAll()
//                                .requestMatchers("").authenticated()
//                                .requestMatchers("").hasAnyAuthority("ADMIN")
//                                .requestMatchers("").hasAnyRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(expHandling-> expHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return https.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userServiceImplementation);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }
}
