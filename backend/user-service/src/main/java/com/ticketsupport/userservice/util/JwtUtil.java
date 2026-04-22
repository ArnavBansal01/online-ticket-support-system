package com.ticketsupport.userservice.util;

import com.ticketsupport.userservice.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor(Constants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    public String generate(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + Constants.JWT_EXPIRY_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
