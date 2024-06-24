package org.conferatus.timetable.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Lesson;

import java.util.List;

public record LessonDTO(
        Long id,
        String name,
        SimpleTeacher teacher,
        AudienceDTO auditory,
        List<StudyGroupResponseDTO> groups,
        @JsonProperty("day_index") int dayNumber,
        @JsonProperty("time_index") Long timeNumber

) {
    public LessonDTO(Lesson lesson) {
        this(
                lesson.getId(),
                "",
                new SimpleTeacher(lesson.getTeacher()),
                new AudienceDTO(lesson.getAudience()),
                lesson.getGroups().stream().map(StudyGroupResponseDTO::new).toList(),
                lesson.getDayOfWeek().ordinal(),
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
