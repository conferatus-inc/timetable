package org.conferatus.timetable.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Lesson;
import org.conferatus.timetable.backend.model.entity.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public record TeacherResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
//        List<SubjectPlanDTO> possibleSubjects,
        List<LessonDTO> lessons
) {
    public TeacherResponseDTO(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getName(),
//                teacher.getPossibleSubjects().stream().map(SubjectPlanDTO::new).collect(Collectors.toList()),
                teacher.getLessons().stream().map(LessonDTO::new).collect(Collectors.toList())
        );
    }
}
