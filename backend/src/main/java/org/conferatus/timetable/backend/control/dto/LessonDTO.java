package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.model.entity.Lesson;

public record LessonDTO(
        String id,
        SimpleTeacher teacher,
        AudienceDTO auditory,
        List<StudyGroupResponseDTO> groups,
        @JsonProperty("day_index") int dayNumber,
        @JsonProperty("time_index") int timeNumber

) {
    public LessonDTO(Lesson lesson) {
        this(String.valueOf(lesson.getId()),
                new SimpleTeacher(lesson.getTeacher()),
                new AudienceDTO(lesson.getAudience()),
                lesson.getGroups()
                        .stream()
                        .map(StudyGroupResponseDTO::new)
                        .toList(),
                lesson.getWeekDay(),
                lesson.getNumberOfTime()
        );
    }

    public LessonDTO(LessonWithTime lessonWithTime) {
        this(
                lessonWithTime.lessonGene().subject().id(),
                new SimpleTeacher(lessonWithTime.teacher()),
                new AudienceDTO(lessonWithTime.audience()),
                lessonWithTime.groups().stream().map(
                        StudyGroupResponseDTO::new
                ).toList(),
                0,
                0
        );
    }
}
