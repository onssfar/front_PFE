package com.example.demo.security.Jwt;

import com.example.demo.security.service.UserPrincipale;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.logging.Logger;
@Slf4j
@Component
public class JwtProvider {


    private String jwtSecret;

    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipale userPrincipal = (UserPrincipale) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token -> Message: {}");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token -> Message: {}");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token -> Message: {}");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty -> Message: {}");
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }



}
