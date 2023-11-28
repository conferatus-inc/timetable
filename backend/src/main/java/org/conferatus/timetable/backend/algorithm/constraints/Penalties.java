package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.model.AudienceType;

import java.util.function.Function;

/**
 * negative if penalty
 * large negative if it's hard constraint
 * small penalty if it's weak constraint
 * positive if good ending
 */
public enum Penalties {
    TeacherAndAudienceType(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                if (!lesson.audience().auditoryType()
                        .equals(lesson.teacher().teacherType())) {
                    return -5.;
                }
                return 0.;
            }
    ),
    AudienceUnique(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var lessonsInCell = data.getLessonsInCell(lesson);
                //audit unique
                if (lessonsInCell.stream()
                        .anyMatch(lessonWithTime ->
                                lessonWithTime.audience().id()
                                        .equals(lesson.audience().id()))) {
                    return -5.;
                }
                return 0.;
            }
    ),
    TeacherUnique(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var lessonsInCell = data.getLessonsInCell(lesson);
                if (lessonsInCell.stream()
                        .anyMatch(lessonWithTime ->
                                lessonWithTime.teacher().id()
                                        .equals(lesson.teacher().id()))) {
                    return -5.;
                }
                return 0.;
            }
    ),
    GroupUnique(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var lessonsInCell = data.getLessonsInCell(lesson);
                if (!lesson.audience().auditoryType().equals(AudienceType.LECTURE)
                        && lessonsInCell.stream()
                        .anyMatch(lessonWithTime ->
                                lessonWithTime.groups().stream().anyMatch(
                                        lesson.groups()::contains
                                ))
                ) {
                    return -5.;
                }
                return 0.;
            }
    ),
    ;
    final Function<GeneticAlgorithmScheduler.DataForConstraint, Double> penaltyFunction;

    public Function<GeneticAlgorithmScheduler.DataForConstraint, Double> getPenaltyFunction() {
        return penaltyFunction;
    }

    Penalties(Function<GeneticAlgorithmScheduler.DataForConstraint, Double> penaltyFunction) {
        this.penaltyFunction = penaltyFunction;
    }

}
