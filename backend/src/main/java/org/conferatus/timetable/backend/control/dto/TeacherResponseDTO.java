package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Teacher;

public record TeacherResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name
) {
    public TeacherResponseDTO(Teacher teacher) {
        this(teacher.getId(),
                teacher.getName());
    }
}
