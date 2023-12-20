package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.entity.SemesterPlan;

public record SemesterPlanDTO(
        Long id,
        String name,
        List<SubjectPlanDTO> subjectPlans,
        List<StudyGroupResponseDTO> studyGroups
) {
    public SemesterPlanDTO(SemesterPlan semesterPlan) {
        this(
                semesterPlan.id(),
                semesterPlan.name(),
                semesterPlan.subjectPlans().stream().map(SubjectPlanDTO::new).toList(),
                semesterPlan.studyGroups().stream().map(StudyGroupResponseDTO::new).toList()
        );
    }
}
