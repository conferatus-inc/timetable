package org.conferatus.timetable.backend.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.entity.TeacherWish;

public record SimpleTeacher(
        Long id,
        String name,
        List<TeacherWishDto> teacherWishes
) {
    public SimpleTeacher(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getName(),
                teacher.getTeacherWishes().stream().map(TeacherWish::toWishDto).toList()
        );
    }

}
