package org.conferatus.timetable.backend.algorithm.scheduling;

import org.conferatus.timetable.backend.model.TableTime;

import java.util.List;

public record LessonWithTime(AudienceTimeCell cell, LessonGene lessonGene) {
    public AudienceEvolve audience() {
        return cell.audience();
    }

    public TableTime time() {
        return cell.time();
    }

    public TeacherEvolve teacher() {
        return lessonGene.teacher();
    }

    public List<GroupEvolve> groups() {
        return lessonGene.groups();
    }

    public SubjectEvolve subject() {
        return lessonGene.subject();
    }

    @Override
    public String toString() {
        return cell.audience() +
                ":" + lessonGene;
    }

}