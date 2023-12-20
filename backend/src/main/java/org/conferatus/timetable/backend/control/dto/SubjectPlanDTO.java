package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;

public record SubjectPlanDTO(
        Long id,
        Long times,
        String name,
        SubjectType subjectType,
        List<SimpleSubjectTeacher> teachers
) {
    public SubjectPlanDTO(SubjectPlan subjectPlan) {
        this(
                subjectPlan.id(),
                subjectPlan.times(),
                subjectPlan.name(),
                subjectPlan.subjectType(),
                subjectPlan.teachers().stream().map(SimpleSubjectTeacher::new).toList()
        );
    }
}
