package org.conferatus.timetable.backend.control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.AuditoryType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AudienceResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("type") AuditoryType type


) {
    public AudienceResponseDTO(Audience audience) {
        this(audience.getId(),
                audience.getName(),
                audience.getAuditoryType());
    }
}
