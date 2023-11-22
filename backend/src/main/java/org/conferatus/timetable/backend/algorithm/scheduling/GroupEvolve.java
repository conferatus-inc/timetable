package org.conferatus.timetable.backend.algorithm.scheduling;

public record GroupEvolve(String id) {
    @Override
    public String toString() {
        return id;
    }
}
