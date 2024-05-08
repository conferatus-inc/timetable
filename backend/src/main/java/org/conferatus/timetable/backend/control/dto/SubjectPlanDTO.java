package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.enums.SubjectType;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;

public record SubjectPlanDTO(
        Long id,
        Long times,
        String name,
        SubjectType subjectType,
        Teacher teacher
) {
    public SubjectPlanDTO(SubjectPlan subjectPlan) {
        this(
                subjectPlan.id(),
                subjectPlan.timesPerWeek(),
                subjectPlan.name(),
                subjectPlan.subjectType(),
                subjectPlan.teacher()
        );
    }
}
