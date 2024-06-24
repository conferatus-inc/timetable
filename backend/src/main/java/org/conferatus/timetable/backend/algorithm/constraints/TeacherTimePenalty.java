package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.algorithm.scheduling.TeacherEvolve;
import org.conferatus.timetable.backend.model.enums.TableTime;

import java.util.Set;

import static org.conferatus.timetable.backend.algorithm.constraints.CalculateResult.ok;
import static org.conferatus.timetable.backend.algorithm.constraints.CalculateResult.problem;

public class TeacherTimePenalty extends Penalty {
    private static final double penalty = -0.1;

    public TeacherTimePenalty(Set<TableTime> tableTimes, TeacherEvolve teacher) {
        super("TeacherTimePenalty", data -> {
                    LessonWithTime lesson = data.currentLesson();
                    //todo: penalty cost
                    if (tableTimes.contains(lesson.time()) && lesson.teacher().equals(teacher)) {
                        return problem(penalty, String.format("%s has uncomfortable %s", teacher.id(), lesson) + lesson);
                    }
                    return ok();
                },
                false);
    }


}
