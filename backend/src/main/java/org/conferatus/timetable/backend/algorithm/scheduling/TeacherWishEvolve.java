package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.entity.TeacherWish;
import org.conferatus.timetable.backend.model.enums.TableTime;

public record TeacherWishEvolve(TableTime time, double penalty) {
    public TeacherWishEvolve(TeacherWish teacherWish) {
        this(new TableTime(teacherWish.getDayOfWeek().ordinal(),
                        (int) teacherWish.getLessonNumber()),
                teacherWish.getPriority());
    }
}
