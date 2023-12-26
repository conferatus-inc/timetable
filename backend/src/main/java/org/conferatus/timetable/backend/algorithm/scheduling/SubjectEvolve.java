package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.Map;

public record SubjectEvolve(Long id,
                            int seminarAmount,
                            int lectureAmount,
                            Map<Long, TeacherEvolve> groupNameToTeacher,
                            TeacherEvolve lectureTeacherEvolve,
                            int subId) {
    public SubjectEvolve(Long id,
                         int seminarAmount,
                         int lectureAmount,
                         Map<Long, TeacherEvolve> teacherToGroup,
                         TeacherEvolve lectureTeacherEvolve) {
        this(id, seminarAmount, lectureAmount, teacherToGroup, lectureTeacherEvolve, 0);
    }

    public SubjectEvolve withSubId(int subId) {
        return new SubjectEvolve(id, seminarAmount, lectureAmount, groupNameToTeacher, lectureTeacherEvolve, subId);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
