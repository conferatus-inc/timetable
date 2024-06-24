package org.conferatus.timetable.backend.dto;

public record UserDto(
        Long id,
        String username,
        UniversityDto universityDto
) {

}
