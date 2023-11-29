package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.entity.Teacher;

public record SimpleTeacher(
        Long id,
        String name
) {
    public SimpleTeacher(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getName()
        );
    }
}
