package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.entity.Teacher;

import java.util.List;
import java.util.Objects;

public record TeacherEvolve(Long id, List<TeacherWishEvolve> wishes) {
    public TeacherEvolve(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getTeacherWishes().stream().map(TeacherWishEvolve::new).toList()
        );
    }

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
