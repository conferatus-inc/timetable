package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.ArrayList;
import java.util.List;

public record LessonGene(List<GroupEvolve> groups, TeacherEvolve teacher, SubjectEvolve subject) {
    public LessonGene(GroupEvolve groupEvolve, TeacherEvolve teacherEvolve, SubjectEvolve subjectEvolve) {
        this(new ArrayList<>(List.of(groupEvolve)), teacherEvolve, subjectEvolve);
    }

    @Override
    public String toString() {
        return "{G:" + groups + "}" +
                " {T:" + teacher + "}" +
                " {S:" + subject + "}";
    }
}