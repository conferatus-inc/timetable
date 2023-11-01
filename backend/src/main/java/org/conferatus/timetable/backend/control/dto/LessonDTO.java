package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.entity.Lesson;

public record LessonDTO() {
    public LessonDTO(Lesson lesson) {
        this();
    }
}
