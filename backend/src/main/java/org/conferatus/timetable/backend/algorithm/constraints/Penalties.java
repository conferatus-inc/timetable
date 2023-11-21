package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;

import java.util.function.Function;

public enum Penalties {
    AuditoryType(
            (data) -> {
                var lessons = data.allLessons();
                LessonWithTime lesson = data.currentLesson();
                if (!lesson.cell().auditory().auditoryType
                        .equals(lesson.lessonGene().teacherEvolve().teacherType)) {
                    return -5.;
                }
                return 0.;
            }
    ),
    AuditoryUnique(
            (data) -> {
                var lessons = data.allLessons();
                LessonWithTime lesson = data.currentLesson();
                //audit unique
                if (lessons.stream()
                        .anyMatch(lessonWithTime -> !lessonWithTime.equals(lesson)
                                && lessonWithTime.cell().auditory().id.equals(lesson.cell().auditory().id))) {
                    return -5.;
                }
                return 0.;
            }
    ),
    TeacherUnique(
            (data) -> {
                var lessons = data.allLessons();
                LessonWithTime lesson = data.currentLesson();
                if (lessons.stream()
                        .anyMatch(lessonWithTime -> !lessonWithTime.equals(lesson)
                                && lessonWithTime.lessonGene().teacherEvolve().id
                                .equals(lesson.lessonGene().teacherEvolve().id))) {
                    return -5.;
                }
                return 0.;
            }
    ),
    GroupUnique(
            (data) -> {
                var lessons = data.allLessons();
                LessonWithTime lesson = data.currentLesson();
                if (!lesson.cell().auditory().auditoryType.equals(AudienceEvolve.AuditoryType.LECTURE)
                        && lessons.stream()
                        .anyMatch(lessonWithTime -> !lessonWithTime.equals(lesson)
                                && lessonWithTime.lessonGene().groupEvolves().stream().anyMatch(
                                lesson.lessonGene().groupEvolves()::contains
                        ))
                ) {
                    return -5.;
                }
                return 0.;
            }
    ),
    ;
    final Function<GeneticScheduler.DataForConstraint, Double> penaltyFunction;

    public Function<GeneticScheduler.DataForConstraint, Double> getPenaltyFunction() {
        return penaltyFunction;
    }

    Penalties(Function<GeneticScheduler.DataForConstraint, Double> penaltyFunction) {
        this.penaltyFunction = penaltyFunction;
    }

}
