package org.conferatus.timetable.backend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.conferatus.timetable.backend.exception.ServerExceptions;
import org.conferatus.timetable.backend.model.entity.Role;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.RoleName;
import org.conferatus.timetable.backend.services.AuthService;
import org.conferatus.timetable.backend.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService accountService;
    private final JwtUtils jwtUtils;

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestHeader(value = "Authorization") String token,
            @RequestHeader(value = "role") RoleName role
    ) {
        var userLoginDTO = accountService.login(token, role);
        var yResponse = userLoginDTO.getResponseYandexId();
        var user = userLoginDTO.getUser();
        var tokens = jwtUtils.createTokens(user);
        return ResponseEntity.ok(
                Map.of(
                        "access_token", tokens.accessToken(),
                        "refresh_token", tokens.refreshToken(),
                        "roles", user.getRoles().stream().map(Role::getName).toList(),
                        "username", user.getUsername(),
                        "login", yResponse.login()
                )
        );
    }

    @PostMapping("/token/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("User trying to refresh token");
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                var refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = null;
                try {
                    decodedJWT = jwtUtils.decodeJWT(refreshToken);
                } catch (TokenExpiredException e) {
                    // TODO: добавить мб другие кэтчи, или обобщить по-другому
                    ServerExceptions.REFRESH_TOKEN_EXPIRED.throwException();
                } catch (JWTVerificationException e) {
                    log.error("Error refresh");
                    ServerExceptions.BAD_REFRESH_TOKEN.throwException();
                }
                var username = decodedJWT.getSubject();
                var user = accountService.getUser(username);
                var oldRefreshToken = user.getRefreshToken();
                if (!Objects.equals(oldRefreshToken, refreshToken)) {
                    ServerExceptions.NOT_CURRENT_REFRESH_TOKEN.moreInfo("NOT current refresh token").throwException();
                }
                var tokens = jwtUtils.createTokens(user);

                accountService.updateAccessToken(username, tokens.accessToken());
                accountService.updateRefreshToken(username, tokens.refreshToken());
                response.setHeader("access_token", tokens.accessToken());
                response.setHeader("refresh_token", tokens.refreshToken());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        Map.of(
                                "access_token", tokens.accessToken(),
                                "refresh_token", tokens.refreshToken(),
                                "roles", user.getRoles().stream().map(Role::getName).toList(),
                                "username", user.getUsername()
                        )
                );
            } catch (IOException e) {
                log.error("Error logging with {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(
                            response.getOutputStream(),
                            error
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION");
            response.setStatus(ServerExceptions.BAD_REFRESH_TOKEN.status());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServerExceptions.BAD_REFRESH_TOKEN.moreInfo("REFRESH TOKEN DOESN'T PROVIDED");
        }
    }
}
