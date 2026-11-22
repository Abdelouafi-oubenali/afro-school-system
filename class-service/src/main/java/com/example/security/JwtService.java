package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    private volatile boolean secretHashLogged = false;

    public String extractUsername(String token) {
        logJwtParts(token);
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.after(new Date());
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");
        if (roles instanceof Collection<?> collection) {
            return collection.stream()
                    .map(String::valueOf)
                    .toList();
        }
        if (roles instanceof String str) {
            return List.of(str.split(","));
        }
        return List.of();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret est vide");
        }
        logSecretHashOnce();
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    private void logJwtParts(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            logger.debug("JWT header: {}", header);
            logger.debug("JWT payload: {}", payload);
            logger.debug("JWT signature: {}", signature);
        }
    }

    private void logSecretHashOnce() {
        if (secretHashLogged) {
            return;
        }
        synchronized (this) {
            if (secretHashLogged) {
                return;
            }
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
                String hashBase64 = Base64.getEncoder().encodeToString(hash);
                logger.info("JWT secret hash (SHA-256, Base64): {}", hashBase64);
            } catch (Exception ex) {
                logger.warn("Impossible de calculer le hash du secret JWT: {}", ex.getMessage());
            }
            secretHashLogged = true;
        }
    }
}
