package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.AudienceType;

public record AudienceEvolve(String id,
                             AudienceType auditoryType) {

    @Override
    public String toString() {
        return id + ":" + auditoryType.name().substring(0, 2);
    }
}
