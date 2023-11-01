package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record TimeTable(
        @JsonProperty("study_days_per_week") List<String> days,
        @JsonProperty("lessons_per_day") int lessonsPerDay,
        Map<String, Map<Integer, LessonDTO>> lessons
) {


}
