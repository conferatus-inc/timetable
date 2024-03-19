package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TimeListDTO(
        @JsonProperty("days_name") List<String> days,
        @JsonProperty("study_days_per_week") int daysPerWeek,
        @JsonProperty("lessons_per_day") int lessonsPerDay,
        List<LessonDTO> lessonsList
) {


}
