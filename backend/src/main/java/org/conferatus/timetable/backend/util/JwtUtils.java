package org.conferatus.timetable.backend.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtils {
    private final AuthService accountService;

    private static final Long accessTokenLifetime = (long) (5000 * 60 * 1000);
    private static final Long refreshTokenLifetime = (long) (5000 * 60 * 1000);

    @Value("${security.secret}")
    private String secret = null;

    public Tokens createTokens(User user) {
        var username = user.getUsername();
        var algorithm = Algorithm.HMAC256(secret.getBytes());
        var accessToken =
                JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenLifetime))
                        .withIssuer("access")
                        .withIssuedAt(new Date())
                        .withClaim(
                                "roles",
                                user.getRoles().stream().map(role -> role.getName().name()).toList()
                        )
                        .sign(algorithm);
        var refreshToken =
                JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenLifetime))
                        .withIssuer("refresh")
                        .withIssuedAt(new Date())
                        .withClaim(
                                "roles",
                                user.getRoles().stream().map(role -> role.getName().name()).toList()
                        )
                        .sign(algorithm);
        accountService.updateAccessToken(username, accessToken);
        accountService.updateRefreshToken(username, refreshToken);
        return new Tokens(accessToken, refreshToken);
    }

    public DecodedJWT decodeJWT(String token) {
        var algorithm = Algorithm.HMAC256(secret);
        var verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public record Tokens(
            String accessToken,
            String refreshToken
    ) {
    }
}
