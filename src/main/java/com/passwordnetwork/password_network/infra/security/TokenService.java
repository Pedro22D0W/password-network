package com.passwordnetwork.password_network.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.passwordnetwork.password_network.Models.User;
import org.springframework.beans.factory.annotation.Value;



@Service

public class TokenService {
@Value("${api.secret.token.secret:defaultSecret}")
private String secret;
    public String GenerateToken(User user){
        
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer("password-network-api")
                .withSubject(user.getEmail())
                .withExpiresAt(GenerateExpiration())
                .sign(algorithm);
            return token;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("token generate token error");
        }
    }

    public String ValidateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                .withIssuer("password-network-api")
                .build()
                .verify(token)
                .getSubject();

        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant GenerateExpiration(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }
}
