package org.conferatus.timetable.backend.dto;

import java.util.List;

public record LoginDto(
        Long id,
        String username,
        String login,
        String accessToken,
        String refreshToken,
        List<RoleDto> roles
) {
}
