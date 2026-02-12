package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JWTUtil {
    @Value("${spring.security.jwt.token.secret}")
    private  String SECRET_KEY;
    @Value("${spring.security.jwt.token.access-expiration}")
    private  Long EXPIRATION_TIME;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));
        claims.put("roles",roles);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Claims extractingBodyFromToken(String token){
       return Jwts.parserBuilder()
               .setSigningKey(SECRET_KEY.getBytes())
               .build()
               .parseClaimsJws(token)
               .getBody();

    }
    public boolean isExpiredToken(String token){
       return extractingBodyFromToken(token).getExpiration().before(Date.from(Instant.now()));

    }
    public boolean validateToken(String token, UserDetails userDetails){
        String tokenUser = extractingBodyFromToken(token).getSubject();
        return Objects.equals(tokenUser,userDetails.getUsername()) && !isExpiredToken(token);
    }

}
