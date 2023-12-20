package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.model.entity.SubjectTeacher;

public record SimpleSubjectTeacher(
        Long id,
        SimpleTeacher teacher,
        Long possibleTimes
) {
    public SimpleSubjectTeacher(SubjectTeacher teacher) {
        this(
                teacher.id(),
                new SimpleTeacher(teacher.teacher()),
                teacher.possibleTimes()
        );
    }
}
