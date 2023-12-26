package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.AudienceType;

public record AudienceDTO(
        Long id,
        String name,
        AudienceType type
) {

}
