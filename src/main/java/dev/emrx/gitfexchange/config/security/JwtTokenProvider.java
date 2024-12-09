package dev.emrx.gitfexchange.config.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import dev.emrx.gitfexchange.users.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private Long jwtDurationSeconds;

    public String generateToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    return Jwts.builder()
            .header()
                .type("JWT")
                .and()
            .subject(Long.toString(user.getId()))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + (jwtDurationSeconds * 1000)))
            .claim("username", user.getUsername())
            .claim("email", user.getEmail())
            .signWith(key, Jwts.SIG.HS256)  // Changed from RS256 to HS256 to match the key type
            .compact();
    }

    public boolean isValidToken(String token) {
        if (!StringUtils.hasLength(token))
            return false;

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.info("Error en la firma del token", e);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.info("Token incorrecto", e);
        } catch (ExpiredJwtException e) {
            log.info("Token expirado", e);
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }
}
