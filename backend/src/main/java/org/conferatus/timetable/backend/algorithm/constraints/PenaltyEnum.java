package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.GroupEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;

import java.util.concurrent.atomic.AtomicReference;

import static org.conferatus.timetable.backend.algorithm.constraints.CalculateResult.ok;
import static org.conferatus.timetable.backend.algorithm.constraints.CalculateResult.problem;

/**
 * negative if penalty
 * large negative if it's hard constraint
 * small penalty if it's weak constraint
 * positive if good ending
 */
public enum PenaltyEnum {
    TeacherWish(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var maybeWish = lesson.teacher().wishes().stream().filter(wish -> wish.time().equals(lesson.time())).findAny();
                if (maybeWish.isPresent()) {
                    var wish = maybeWish.get();
                    return wish.penalty() >= 0
                            ? ok(wish.penalty())
                            : problem(wish.penalty(), "Teacher {%s} doesn't want this time {%s}"
                            .formatted(lesson.teacher().toString(), lesson.time()));
                }
                return ok();
            },
            true
    ),
    AudienceUnique(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var lessonsInCell = data.getOtherLessons(lesson);
                //audit unique
                if (lessonsInCell.stream()
                        .anyMatch(lessonWithTime ->
                                lessonWithTime.audience().id()
                                        .equals(lesson.audience().id()))) {
                    return problem(-100., "Problem with audience " + lesson.audience());
                }
                return ok();
            },
            true
    ),
    TeacherUnique(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var lessonsInCell = data.getOtherLessons(lesson);
                if (lessonsInCell.stream()
                        .anyMatch(lessonWithTime ->
                                lessonWithTime.teacher().id()
                                        .equals(lesson.teacher().id()))) {
                    return problem(-100., "Teacher teach in on cell " + lesson.teacher());
                }
                return ok();
            },
            true
    ),
    GroupsInOneAuditory(
            (data) -> {
                LessonWithTime lesson = data.currentLesson();
                var otherLessons = data.getOtherLessons(lesson);
                AtomicReference<GroupEvolve> groupEvolve = new AtomicReference<>(new GroupEvolve((Long) null));
                AtomicReference<LessonWithTime> lessonRef = new AtomicReference<>(null);
                if (otherLessons.stream()
                        .anyMatch(lessonWithTime ->
                                {
                                    for (GroupEvolve group : lessonWithTime.groups()) {
                                        if (lesson.groups().contains(group)) {
                                            groupEvolve.set(group);
                                            lessonRef.set(lessonWithTime);
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                        )) {
                    return problem(-100.,
                            "Lesson " + lesson + " problem with groups " + groupEvolve.get()
                                    + " to lesson " + lessonRef.get());
                }
                return ok();
            },
            true
    ),
    WorkingTime(
            data -> {
                LessonWithTime lesson = data.currentLesson();
                var otherLessons = data.getOtherLessons(lesson);
                var time = lesson.time();
                if (time.cellNumber() == 1 || time.cellNumber() > 4) {
                    return problem(-0.5, "The student will not like the time " + lesson);
                }
                if (time.day() >= 6) {
                    return problem(-0.5, "It's saturday, bro " + lesson);
                }
                return ok();
            }
            , false
    ),
    AudienceFullness(
            data -> {
                double baseVal = 1;
                LessonWithTime lesson = data.currentLesson();
                double roomCapacity = lesson.cell().audience().groupCapacity();
                double groupsAmount = lesson.groups().size();
                double value = baseVal * 1 - groupsAmount / roomCapacity;
                if (value >= 0.666 * baseVal) {
                    return problem(-value * 2, "Audience is almost empty");
                }
                return ok(-value);
            }
            ,
            false
    ),
//    FakePenalty(
//            data -> {
//                LessonWithTime lesson = data.currentLesson();
//                if (lesson.teacher().teacherType().equals(AudienceType.LECTURE)) {
//                    return problem(-0.05, "Student don't like lecture " + lesson);
//                }
//                return ok();
//            }
//            , false
//    ),

    ;
    final PenaltyFunction penaltyFunction;
    final boolean isHard;
    final String name;

    public PenaltyFunction getPenaltyFunction() {
        return penaltyFunction;
    }

    public Penalty toPenalty() {
        return new Penalty(name, penaltyFunction, isHard);
    }

    PenaltyEnum(PenaltyFunction penaltyFunction, boolean isHard, String name) {
        this.penaltyFunction = penaltyFunction;
        this.isHard = isHard;
        this.name = name;
    }

    PenaltyEnum(PenaltyFunction penaltyFunction, boolean isHard) {
        this.penaltyFunction = penaltyFunction;
        this.isHard = isHard;
        this.name = this.name();
    }

}
