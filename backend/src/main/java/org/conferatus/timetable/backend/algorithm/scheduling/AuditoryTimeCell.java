package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.TableTime;

public record AuditoryTimeCell(AudienceEvolve audience, TableTime time) {
    public AuditoryTimeCell(AudienceEvolve audience, int timeIndex) {
        this(audience, new TableTime(timeIndex));
    }
}