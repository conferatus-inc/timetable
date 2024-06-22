package org.conferatus.timetable.backend.dto;

import java.time.DayOfWeek;

public record TeacherWishDto(
        Long id,
        DayOfWeek dayOfWeek,
        long lessonNumber,
        long priority
) {
}
