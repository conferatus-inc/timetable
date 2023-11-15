package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Teacher {
    public String id;
    public AuditoryEvolve.AuditoryType teacherType;

    @Override
    public String toString() {
        return id + ':' + teacherType;
    }
}
