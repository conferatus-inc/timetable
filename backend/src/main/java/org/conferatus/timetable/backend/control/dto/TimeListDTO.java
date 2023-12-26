package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TimeListDTO(
        Long id,
        @JsonProperty("days_name") List<String> days,
        @JsonProperty("study_days_per_week") int daysPerWeek,
        @JsonProperty("lessons_per_day") int lessonsPerDay,
        List<LessonDTO> cells
) {


}
