package org.conferatus.timetable.backend.algorithm.scheduling;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public class StudyPlanEvolve {
    public List<SubjectEvolve> subjectEvolves;
    public List<GroupEvolve> groupEvolves;
}
