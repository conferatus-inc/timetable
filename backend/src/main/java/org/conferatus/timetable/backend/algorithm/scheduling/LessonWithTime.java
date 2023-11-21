package org.conferatus.timetable.backend.algorithm.scheduling;

public record LessonWithTime(AuditoryTimeCell cell, LessonGene lessonGene) {
    @Override
    public String toString() {
        return cell.auditory() +
                ":" + lessonGene;
    }
}