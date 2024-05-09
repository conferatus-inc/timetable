package org.conferatus.timetable.backend.dto;

import org.conferatus.timetable.backend.model.entity.University;

public record UniversityDto(
        Long id,
        String name
) {
    public UniversityDto(University university) {
        this(
                university.id(),
                university.name()
        );
    }
}
