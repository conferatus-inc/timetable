package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.model.entity.Lesson;
import org.conferatus.timetable.backend.model.entity.Subject;
import org.conferatus.timetable.backend.model.entity.Teacher;

public record SubjectDTO (
    Long id,
    String name,
    List<Teacher> possibleTeacher,
    List<Lesson> lessons
) {
    public SubjectDTO(Subject subject) {
        this(
                subject.getId(),
                subject.getName(),
                subject.getPossibleTeacher(),
                subject.getLessons()
        );
    }
}
