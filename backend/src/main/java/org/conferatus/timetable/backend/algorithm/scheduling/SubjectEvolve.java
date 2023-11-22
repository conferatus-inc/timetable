package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.List;

public record SubjectEvolve(String id,
                            int seminarAmount,
                            int lectureAmount,
                            List<TeacherEvolve> seminarTeacherEvolve,
                            TeacherEvolve lectureTeacherEvolve,
                            int subId) {
    public SubjectEvolve(String id,
                         int seminarAmount,
                         int lectureAmount,
                         List<TeacherEvolve> seminarTeacherEvolve,
                         TeacherEvolve lectureTeacherEvolve) {
        this(id, seminarAmount, lectureAmount, seminarTeacherEvolve, lectureTeacherEvolve, 0);
    }

    public SubjectEvolve withSubId(int subId) {
        return new SubjectEvolve(id, seminarAmount, lectureAmount, seminarTeacherEvolve, lectureTeacherEvolve, subId);
    }

    @Override
    public String toString() {
        return id;
    }
}
