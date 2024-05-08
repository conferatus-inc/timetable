package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceEvolve(Long id,
                             long audienceGroupCapacity) {

    public AudienceEvolve(Audience audience) {
        this(
                audience.getId(),
                audience.getAudienceGroupCapacity()
        );
    }

    @Override
    public String toString() {
        return id + ":" + audienceGroupCapacity;
    }
}
