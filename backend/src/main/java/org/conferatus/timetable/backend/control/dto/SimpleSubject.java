package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.model.entity.Subject;

public record SimpleSubject(
        Long id,
        String name,
        SubjectType subjectType
) {
    public SimpleSubject(Subject subject) {
        this(
                subject.getId(),
                subject.getName(),
                subject.getSubjectType()
        );
    }
}
