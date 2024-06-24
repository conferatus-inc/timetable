package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.entity.Audience;

import java.util.Objects;

public record AudienceEvolve(
        Long id,
        long groupCapacity,
        String name
) implements Comparable<AudienceEvolve>{

    public AudienceEvolve(Audience audience) {
        this(
                audience.getId(),
                audience.getAudienceGroupCapacity(),
                audience.getName()
        );
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudienceEvolve that = (AudienceEvolve) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(AudienceEvolve o) {
        return Long.compare(groupCapacity, o.groupCapacity);
    }
}
