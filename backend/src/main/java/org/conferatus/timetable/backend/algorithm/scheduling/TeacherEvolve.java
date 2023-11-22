package org.conferatus.timetable.backend.algorithm.scheduling;


import org.conferatus.timetable.backend.model.AudienceType;

public record TeacherEvolve(String id, AudienceType teacherType) {
    @Override
    public String toString() {
        return id + ':' + teacherType;
    }
}
