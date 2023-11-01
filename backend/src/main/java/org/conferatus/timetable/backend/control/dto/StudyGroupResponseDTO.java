package org.conferatus.timetable.backend.control.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record StudyGroupResponseDTO(
        @JsonProperty("name") String name


) {
}
