package org.conferatus.timetable.backend.dto;

import java.time.DayOfWeek;

import org.conferatus.timetable.backend.model.entity.TeacherWish;

public record TeacherWishDto(
        DayOfWeek dayOfWeek,
        long lessonNumber,
        long priority
) {

    public TeacherWishDto(TeacherWish teacherWish) {
        this(teacherWish.getDayOfWeek(), teacherWish.getLessonNumber(), teacherWish.getPriority());
    }
}
