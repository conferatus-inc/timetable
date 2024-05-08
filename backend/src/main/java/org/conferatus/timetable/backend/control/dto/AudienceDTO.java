package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceDTO(
        Long id,
        String name,
        long audienceGroupCapacity
) {

    public AudienceDTO(Audience audience) {
        this(audience.getId(), audience.getName(), audience.getAudienceGroupCapacity());
    }
}
