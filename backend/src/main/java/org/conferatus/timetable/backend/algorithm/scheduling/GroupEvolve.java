package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.entity.StudyGroup;

public record GroupEvolve(String id) {

    public GroupEvolve(StudyGroup group) {
        this(
                group.getName()
        );
    }

    @Override
    public String toString() {
        return id;
    }
}
