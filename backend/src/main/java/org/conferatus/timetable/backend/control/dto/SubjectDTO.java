package org.conferatus.timetable.backend.control.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.model.entity.Subject;

public record SubjectDTO(
        Long id,
        String name,
        SubjectType subjectType,
        List<TeacherResponseDTO> possibleTeacher,
        List<LessonDTO> lessons
) {
    public SubjectDTO(Subject subject) {
        this(
                subject.getId(),
                subject.getName(),
                subject.getSubjectType(),
                subject.getPossibleTeachers().stream().map(TeacherResponseDTO::new).collect(Collectors.toList()),
                subject.getLessons().stream().map(LessonDTO::new).collect(Collectors.toList())
        );
    }
}
