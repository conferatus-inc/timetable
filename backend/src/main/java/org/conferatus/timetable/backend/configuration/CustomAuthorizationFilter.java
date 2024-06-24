package org.conferatus.timetable.backend.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.exception.ServerExceptions;
import org.conferatus.timetable.backend.model.enums.RoleName;
import org.conferatus.timetable.backend.repository.RoleRepository;
import org.conferatus.timetable.backend.services.AuthService;
import org.conferatus.timetable.backend.services.UniversityService;
import org.conferatus.timetable.backend.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AuthService accountService;
    private final JwtUtils jwtUtils;
    private final UniversityService universityService;
    private final RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
//        response.addHeader("Access-Control-Allow-Headers", "username, password, role, content-type, Origin,
//        Authorization, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method,
//        Access-Control-Request-Headers");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, " +
                "Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }

        if (accountService.notNeedAuth(request.getServletPath())) {
            log.info("Without authorization {}", request.getServletPath());
            try {
                filterChain.doFilter(request, response);
            } catch (ServerException serverException) {
                log.error(new ObjectMapper().writeValueAsString(serverException.getAnswer()));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(serverException.getStatus().value());
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        serverException.getCode() + " " + serverException.getMessage()
                );
            }
        } else {
            log.info("With authorization {}", request.getServletPath());
            var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    var token = authorizationHeader.substring("Bearer ".length());
                    var decodedJWT = jwtUtils.decodeJWT(token);
                    var username = decodedJWT.getSubject();
                    log.info("user trying authorize: {}", username);
                    var user = accountService.getUser(username);
                    var oldToken = user.getAccessToken();
                    if (oldToken == null) {
                        log.warn("There is no access token for {}", username);
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("There is no access token for $username")
                                .throwException();
                    }
                    if (oldToken.isEmpty()) {
                        log.warn("There is no access token for {}", username);
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("Refresh your token").throwException();
                    }

                    if (!oldToken.equals(token)) {
                        log.warn("It's not current access token {}", username);
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("It's not current access token").throwException();
                    }

                    var roles = decodedJWT.getClaim("roles").asArray(String.class);
                    var authorities = new ArrayList<SimpleGrantedAuthority>();
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                    var authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, authorities, authorities);

                    if (!accountService.notAdminAuth(request.getServletPath())) {
                        authAdmin(List.of(roles));
                    }

                    var copy = SecurityContextHolder.getContext();
                    copy.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(copy);
                    log.info("User {} authorized", username);
                    filterChain.doFilter(request, response);
                } catch (ServerException e) {
                    log.warn("ServerException exception {}", e.getAnswer());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(e.getStatus().value());
                    new ObjectMapper().writeValue(
                            response.getOutputStream(),
                            e.getAnswer()
                    );
                } catch (TokenExpiredException e) {
                    log.warn(
                            "Token expired {}",
                            request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length())
                    );
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(ServerExceptions.ACCESS_TOKEN_PROBLEM.status());
                    new ObjectMapper().writeValue(
                            response.getOutputStream(),
                            ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("Token expired").getAnswer()
                    );
                } catch (JWTVerificationException e) {
                    log.error("Error logging in {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(ServerExceptions.ILLEGAL_ACCESS_TOKEN.status());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(
                            response.getOutputStream(),
                            ServerExceptions.ILLEGAL_ACCESS_TOKEN.getAnswer()
                    );
                }
            } else {
                log.info("NOT TOKEN AUTHENTICATION");
                response.setStatus(ServerExceptions.NO_ACCESS_TOKEN.status());
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        ServerExceptions.NO_ACCESS_TOKEN.getAnswer()
                );
            }
        }
    }

    private void authAdmin(List<String> roles) {
        if (!roles.contains(RoleName.ROLE_ADMIN.name())) {
            ServerExceptions.UNAUTHORIZED
                    .moreInfo("Only ADMIN can access this endpoint").throwException();
        }
    }
}