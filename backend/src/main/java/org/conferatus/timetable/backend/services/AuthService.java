package org.conferatus.timetable.backend.services;

import java.util.HashSet;
import java.util.Set;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.conferatus.timetable.backend.dto.UserLoginDto;
import org.conferatus.timetable.backend.exception.ServerExceptions;
import org.conferatus.timetable.backend.model.entity.Role;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.RoleName;
import org.conferatus.timetable.backend.repository.RoleRepository;
import org.conferatus.timetable.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import static org.conferatus.timetable.backend.configuration.WebSecurityConfiguration.LOGIN_URL;
import static org.conferatus.timetable.backend.configuration.WebSecurityConfiguration.REFRESH_URL;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final YandexIdService yandexIdService;

    private static final Set<String> notAuthURLs = Set.of(LOGIN_URL, REFRESH_URL);

    public static final String UNIVERSITY_URL = "/api/v1/admin/universities";
    public static final String UNIVERSITY_URL_LINK = "/api/v1/admin/universities/link";

    private static final Set<String> adminURLs = Set.of(
            UNIVERSITY_URL,
            UNIVERSITY_URL_LINK
    );

    public boolean notNeedAuth(String url) {
        return notAuthURLs.contains(url);
    }

    public boolean notAdminAuth(String url) {
        return !adminURLs.contains(url);
    }

    public UserLoginDto login(
            String token,
            RoleName role
    ) {
        log.info("starting logging with token:{} and role {}", token.substring(0, 20), role);
        var response = yandexIdService.getId(yandexIdService.parseToken(token));
        var id = response.id();
        var userO = userRepository.findByUsername(id);
        if (userO == null) {
            var newUser = new User();
            newUser.setUsername(id);
            var roleFound = roleRepository.findByName(role);
            if (roleFound == null) {
                ServerExceptions.ROLE_NOT_EXISTS.throwException();
            }
            var userRole = roleRepository.findByName(RoleName.ROLE_USER);
            if (userRole == null) {
                ServerExceptions.ROLE_NOT_EXISTS.moreInfo("ROLE_USER not exists").throwException();
            }
            var roles  = new HashSet<Role>();
            roles.add(userRole);
            roles.add(roleFound);
            newUser.setRoles(roles);
            newUser = userRepository.save(newUser);
            userO = newUser;
        }
        return new UserLoginDto(response, userO);
    }

    public void updateAccessToken(
            String username,
            String accessToken
    ) {
        User user = getUser(username);
        user.setAccessToken(accessToken);
    }

    public void updateRefreshToken(
            String username,
            String refreshToken
    ) {
        User user = getUser(username);
        user.setRefreshToken(refreshToken);
    }

    public User getUser(String username) {
        log.info("Getting user {}", username);
        var user = findUser(username);
        if (user == null) {
            ServerExceptions.USER_NOT_FOUND.moreInfo("User $username not found").throwException();
        }
        return user;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username);
    }
}
