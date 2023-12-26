package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceDTO(
        Long id,
        String name,
        AudienceType type
) {

    public AudienceDTO(Audience audience) {
        this(audience.getId(), audience.getName(), audience.getAudienceType());
    }
}
