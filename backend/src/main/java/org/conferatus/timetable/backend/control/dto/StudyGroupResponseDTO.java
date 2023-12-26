package org.conferatus.timetable.backend.control.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.StudyGroup;

public record StudyGroupResponseDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name


) {
    public StudyGroupResponseDTO(StudyGroup group) {
        this(String.valueOf(group.getId()),
                group.getName());
    }

}
