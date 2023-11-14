package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.entity.Subject;

public record SemesterPlanDTO(
        Long id,
        List<Subject> subjects
) {
    public SemesterPlanDTO(SemesterPlan semesterPlan) {
        this(semesterPlan.getId(), semesterPlan.getSubjects());
    }
}
