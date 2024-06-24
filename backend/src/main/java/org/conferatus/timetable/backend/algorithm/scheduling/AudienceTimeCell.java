package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.enums.TableTime;

import java.util.Objects;

public record AudienceTimeCell(AudienceEvolve audience, TableTime time) implements Comparable<AudienceTimeCell> {
    public AudienceTimeCell(AudienceEvolve audience, int timeIndex) {
        this(audience, new TableTime(timeIndex));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudienceTimeCell that = (AudienceTimeCell) o;
        return Objects.equals(audience, that.audience) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audience, time);
    }

    /**
     * Compare by audienceCapacity
     *
     * @param o the object to be compared.
     * @return compared int depends on audience groupCapacity
     */
    @Override
    public int compareTo(AudienceTimeCell o) {
        return audience.compareTo(o.audience);
    }
}