package org.conferatus.timetable.backend.algorithm.scheduling;

import java.util.List;


public record StudyPlanEvolve(List<SubjectEvolve> subjectEvolves, List<GroupEvolve> groupEvolves) {

}
