package org.conferatus.timetable.backend.control.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.conferatus.timetable.backend.model.entity.SemesterPlan;

public record SemesterPlanDTO(
        Long id,
        List<SubjectDTO> subjects
) {
    public SemesterPlanDTO(SemesterPlan semesterPlan) {
        this(
                semesterPlan.getId(),
                semesterPlan.getSubjects().stream().map(SubjectDTO::new).collect(Collectors.toList())
        );
    }
}
