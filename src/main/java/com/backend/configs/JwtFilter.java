package com.backend.configs;

import com.backend.serviceImpls.UserServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserServiceImplementation userServiceImplementation;

    private final JwtUtils jwtUtils;

   public JwtFilter(
           UserServiceImplementation userServiceImplementation,
           JwtUtils jwtUtils
   ){
       this.jwtUtils= jwtUtils;
       this.userServiceImplementation= userServiceImplementation;
   }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull  FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token!=null){

            String username = jwtUtils.extractUsername(token);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= userServiceImplementation.loadUserByUsername(username);

                if(jwtUtils.isTokenValid(token, userDetails)){

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }


    private static String getTokenFromRequest(HttpServletRequest request){
        String bearerToken= request.getHeader("Authentication");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
