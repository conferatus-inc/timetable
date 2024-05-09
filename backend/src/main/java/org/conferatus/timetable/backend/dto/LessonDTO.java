package org.conferatus.timetable.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Lesson;

import java.time.DayOfWeek;
import java.util.List;

public record LessonDTO(
        Long id,
        String name,
        SimpleTeacher teacher,
        AudienceDTO auditory,
        StudyGroupResponseDTO group,
        @JsonProperty("day_index") DayOfWeek dayNumber,
        @JsonProperty("time_index") Long timeNumber

) {
    public LessonDTO(Lesson lesson) {
        this(lesson.getId(),
                "",
                new SimpleTeacher(lesson.getTeacher()),
                new AudienceDTO(lesson.getAudience()),
                new StudyGroupResponseDTO(lesson.getGroup()),
                lesson.getDayOfWeek(),
                lesson.getLessonNumber()
        );
    }


//    public LessonDTO(LessonWithTime lessonWithTime) {
//        this(
//                lessonWithTime.lessonGene().subject().id(),
//                new SimpleTeacher(lessonWithTime.teacher()),
//                new AudienceDTO(lessonWithTime.audience()),
//                lessonWithTime.groups().stream().map(
//                        StudyGroupResponseDTO::new
//                ).toList(),
//                lessonWithTime.time().day(),
//                lessonWithTime.time().cellNumber()
//        );
//    }
}
