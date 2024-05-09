package org.conferatus.timetable.backend.dto;

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
