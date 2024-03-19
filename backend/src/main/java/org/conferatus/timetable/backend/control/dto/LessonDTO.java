package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Lesson;

import java.util.List;

public record LessonDTO(
        Long id,
        TeacherResponseDTO teacher,
        AudienceDTO auditory,
        List<StudyGroupResponseDTO> groups,
        @JsonProperty("day_index") int dayNumber,
        @JsonProperty("time_index") int timeNumber

) {
    public LessonDTO(Lesson lesson) {
        this(lesson.getId(),
                new TeacherResponseDTO(lesson.getTeacher()),
                new AudienceDTO(lesson.getAudience()),
                lesson.getGroups()
                        .stream()
                        .map(StudyGroupResponseDTO::new)
                        .toList(),
                lesson.getWeekDay(),
                lesson.getNumberOfTime()
        );
    }
}
