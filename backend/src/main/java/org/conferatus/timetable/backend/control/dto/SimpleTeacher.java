package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.algorithm.scheduling.TeacherEvolve;
import org.conferatus.timetable.backend.model.entity.Teacher;

public record SimpleTeacher(
        String id,
        String name
) {
    public SimpleTeacher(Teacher teacher) {
        this(
                String.valueOf(teacher.getId()),
                teacher.getName()
        );
    }

    public SimpleTeacher(TeacherEvolve teacher) {
        this(
                teacher.id(),
                teacher.id()
        );
    }
}
