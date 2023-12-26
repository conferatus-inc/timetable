package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceEvolve(Long id,
                             AudienceType auditoryType) {

    public AudienceEvolve(Audience audience) {
        this(
                audience.getId(),
                audience.getAudienceType()
        );
    }

    @Override
    public String toString() {
        return id + ":" + auditoryType.name().substring(0, 2);
    }
}
