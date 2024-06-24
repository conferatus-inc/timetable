package org.conferatus.timetable.backend.dto;

import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.conferatus.timetable.backend.model.enums.AudienceType;

public record SubjectPlanDTO(
        Long id,
        Long times,
        String name,
        AudienceType subjectType,
        SimpleTeacher teacher
) {
    public SubjectPlanDTO(SubjectPlan subjectPlan) {
        this(
                subjectPlan.id(),
                subjectPlan.timesPerWeek(),
                subjectPlan.name(),
                subjectPlan.subjectType(),
                subjectPlan.teacher().toSimpleTeacher()
        );
    }
}
