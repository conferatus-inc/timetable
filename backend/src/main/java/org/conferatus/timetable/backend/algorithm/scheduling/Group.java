package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Group {
    public String groupId;

    @Override
    public String toString() {
        return groupId;
    }
}
