package org.conferatus.timetable.backend.dto;

import org.conferatus.timetable.backend.model.entity.TeacherWish;

import java.time.DayOfWeek;

public record TeacherWishDto(
        Long id,
        DayOfWeek dayOfWeek,
        long lessonNumber,
        long priority
) {

    public TeacherWishDto(TeacherWish teacherWish) {
        this(teacherWish.getId(), teacherWish.getDayOfWeek(), teacherWish.getLessonNumber(), teacherWish.getPriority());
    }
}
