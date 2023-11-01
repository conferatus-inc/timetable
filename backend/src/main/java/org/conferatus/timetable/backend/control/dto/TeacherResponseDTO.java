package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeacherResponseDTO(
        @JsonProperty("name") String name
) {
}
