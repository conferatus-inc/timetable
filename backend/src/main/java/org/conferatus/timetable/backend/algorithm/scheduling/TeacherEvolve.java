package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.Objects;

public record TeacherEvolve(Long id) {
    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherEvolve that = (TeacherEvolve) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
