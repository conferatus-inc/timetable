package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.entity.StudyGroup;

public record GroupEvolve(Long id) {

    public GroupEvolve(StudyGroup group) {
        this(
                group.getId()
        );
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
