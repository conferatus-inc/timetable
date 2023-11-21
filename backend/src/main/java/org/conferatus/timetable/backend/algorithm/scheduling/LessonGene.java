package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.ArrayList;
import java.util.List;

public record LessonGene(List<GroupEvolve> groupEvolves, TeacherEvolve teacherEvolve, SubjectEvolve subjectEvolve) {
    public LessonGene(GroupEvolve groupEvolve, TeacherEvolve teacherEvolve, SubjectEvolve subjectEvolve) {
        this(new ArrayList<>(List.of(groupEvolve)), teacherEvolve, subjectEvolve);
    }

    @Override
    public String toString() {
        return "{G:" + groupEvolves + "}" +
                " {T:" + teacherEvolve + "}" +
                " {S:" + subjectEvolve + "}";
    }
}