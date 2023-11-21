package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TeacherEvolve {
    public String id;
    public AudienceEvolve.AuditoryType teacherType;

    @Override
    public String toString() {
        return id + ':' + teacherType;
    }
}
