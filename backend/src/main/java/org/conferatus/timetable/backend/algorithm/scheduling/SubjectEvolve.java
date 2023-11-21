package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SubjectEvolve {
    public String id;
    public int seminarAmount;
    public int lectureAmount;
    public List<TeacherEvolve> seminarTeacherEvolve;
    public TeacherEvolve lectureTeacherEvolve;

    @Override
    public String toString() {
        return id;
    }
}
