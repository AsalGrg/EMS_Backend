package com.backend.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long tokenValidity;

    //method to extract all the claims of the token
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //method to get the username from the token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        return new Date().after(extractExpirationDate(token));
    }


    //method to check if the token is valid
    public Boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) &&  !isTokenExpired(token));
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims= extractAllClaims(token);

        return claimResolver.apply(claims);
    }
    private Key getSigninKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateJwtToken(UserDetails userDetails){

        //this claims can contain additional information for the token
        Map<String , String> claims = new HashMap<>();

        return createToken(
                claims,
                userDetails.getUsername(),
                tokenValidity
        );
    }

    public String createToken(
            Map<String, String> claims,
            String subject,
            long tokenValidity
    ) {

        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenValidity)))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
