package org.conferatus.timetable.backend.algorithm.scheduling;


import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.enums.AudienceType;

public record TeacherEvolve(Long id, long audienceGroupCapacity) {
    public TeacherEvolve(Teacher teacher, long audienceGroupCapacity) {
        this(teacher.getId(), audienceGroupCapacity);
    }

    @Override
    public String toString() {
        return id + ":" + audienceGroupCapacity;
    }
}
