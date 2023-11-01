package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record TimeTableDto(
        @JsonProperty("days_name") List<String> days,
        @JsonProperty("study_days_per_week") int daysPerWeek,
        @JsonProperty("lessons_per_day") int lessonsPerDay,
        Map<String, Map<Integer, LessonDTO>> lessonsTable
) {


}
