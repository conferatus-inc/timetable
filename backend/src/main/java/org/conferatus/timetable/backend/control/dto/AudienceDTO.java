package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceDTO(
        String id,
        String name,
        AudienceType type
) {
    public AudienceDTO(Audience audience) {
        this(String.valueOf(audience.getId()), audience.getName(), audience.getAudienceType());
    }

    public AudienceDTO(AudienceEvolve audience) {
        this(
                audience.id(),
                audience.id(),
                audience.auditoryType()
        );
    }
}
