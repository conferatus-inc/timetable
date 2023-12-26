package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LessonDTO(
        Long id,
        SimpleTeacher teacher,
        AudienceDTO auditory,
        List<StudyGroupResponseDTO> groups,
        @JsonProperty("day_index") int dayNumber,
        @JsonProperty("time_index") int timeNumber

) {


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
