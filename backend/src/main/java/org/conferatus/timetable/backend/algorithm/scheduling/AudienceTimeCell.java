package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.TableTime;

public record AudienceTimeCell(AudienceEvolve audience, TableTime time) {
    public AudienceTimeCell(AudienceEvolve audience, int timeIndex) {
        this(audience, new TableTime(timeIndex));
    }
}