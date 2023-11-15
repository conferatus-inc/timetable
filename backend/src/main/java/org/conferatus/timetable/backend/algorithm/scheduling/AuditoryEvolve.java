package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuditoryEvolve {
    public String id;
    public AuditoryType auditoryType;

    public enum AuditoryType {
        LECTURE,
        SEMINAR
    }

    @Override
    public String toString() {
        return "{" + id + ":" + auditoryType + '}';
    }
}
