package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;

public record AuditoryDTO(Long id,
                          String name,
                          AudienceType type) {
    public AuditoryDTO(Audience auditory) {
        this(auditory.getId(), auditory.getName(), auditory.getAudienceType());
    }

}
