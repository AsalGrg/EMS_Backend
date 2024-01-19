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

//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
//
//        https.
//                csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        authHttpRequest-> authHttpRequest.requestMatchers("/register").permitAll()
//                                .requestMatchers("/login").permitAll()
////                                .requestMatchers("").authenticated()
////                                .requestMatchers("").hasAnyAuthority("ADMIN")
////                                .requestMatchers("").hasAnyRole("ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement(
//                        session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authenticationProvider(daoAuthenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(expHandling-> expHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
//
//        return https.build();
//    }
//

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/register")
                                .permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/verify-otp").permitAll()
                                .requestMatchers("/become-vendor-requests").permitAll()
                                .requestMatchers("/addEvent").permitAll()
//                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logout ->
//                        logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
//                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
        ;

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userServiceImplementation);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }
}
