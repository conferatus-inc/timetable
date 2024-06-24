package org.conferatus.timetable.backend.configuration;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration implements WebMvcConfigurer {
    private final CustomAuthorizationFilter customAuthorizationFilter;

    public static final Set<String> NO_AUTH_ENDPOINTS = Set.of(
            "/api/v1/accounts/login",
            "/api/v1/accounts/token/refresh",
            "/error"
    );


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        );

        http.authorizeHttpRequests(request -> {
                    NO_AUTH_ENDPOINTS.forEach(
                            endpoint -> request.requestMatchers(endpoint).permitAll()
                    );
                    request.anyRequest().authenticated();
                }
        );
        http.addFilterAfter(customAuthorizationFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}