package org.conferatus.timetable.backend.control.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Teacher;

public record TeacherResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        List<SimpleSubject> possibleSubjects,
        List<LessonDTO> lessons
) {
    public TeacherResponseDTO(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getName(),
                teacher.getPossibleSubjects().stream().map(SimpleSubject::new).collect(Collectors.toList()),
                teacher.getLessons().stream().map(LessonDTO::new).collect(Collectors.toList())
        );
    }
}
