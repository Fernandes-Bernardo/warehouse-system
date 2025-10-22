package com.almoxarifado.almoxarifado_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long accessTokenExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expiration}") long accessTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
    }

    // Gera Access Token
    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrai username (subject)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai data de expiração
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai qualquer claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Valida se o token é do usuário e não expirou
    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    // Verifica se expirou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}