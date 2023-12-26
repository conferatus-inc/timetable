package org.conferatus.timetable.backend.algorithm.scheduling;


import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.SubjectTeacher;

public record TeacherEvolve(String id, AudienceType teacherType) {
    public TeacherEvolve(SubjectTeacher subjectTeacher, AudienceType teacherType) {
        this(String.valueOf(subjectTeacher.id()), teacherType);
    }

    @Override
    public String toString() {
        return id + ':' + teacherType;
    }
}
