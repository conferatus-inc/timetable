package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.*;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleAlgorithmService algoService;
    private final AudienceService audienceService;
    private final SemesterPlanService semesterPlanService;

    public ScheduleAlgorithmService.StatusId generate(Long semesterId, User user) {
        University university = user.getUniversity();
        SemesterPlan sp = semesterPlanService.getSemesterPlan(user, semesterId);
//        var semesterPlans = semesterPlanService.getSemesterPlan(semesterId);
        if (!sp.university().id().equals(university.id())) {
            throw new ServerException(HttpStatus.FORBIDDEN, "WRONG_UNIVERSITY_SEMESTER");
        }


        List<GroupEvolve> groupEvolves = sp.studyGroups().stream().map(GroupEvolve::new).toList();
        List<SubjectEvolve> subjectEvolves = new ArrayList<>();


        for (SubjectPlan subjectPlan : sp.subjectPlans()) {
            SubjectEvolve subjectEvolve;
            TeacherEvolve teacherEvolve = new TeacherEvolve(subjectPlan.teacher());
            Map<TeacherEvolve, List<GroupEvolve>> teacherToGroups = new HashMap<>();
            teacherToGroups.put(teacherEvolve, new ArrayList<>());


            for (StudyGroup studyGroup : subjectPlan.groups()) {
                teacherToGroups.get(teacherEvolve).add(new GroupEvolve(studyGroup));
            }
            for (long i = 0L; i < subjectPlan.timesPerWeek(); i++) {
                subjectEvolve = new SubjectEvolve(subjectPlan.id(),
                        teacherToGroups,
                        subjectPlan.subjectType().toString(),
                        (int) i);
                subjectEvolves.add(subjectEvolve);
            }
        }
        List<StudyPlanEvolve> studyPlanEvolves = List.of(new StudyPlanEvolve(subjectEvolves, groupEvolves));

        List<AudienceEvolve> mappedAudiences = audienceService.getAllAudiences(university)
                .stream().map(AudienceEvolve::new).toList();
        return algoService.createTaskSchedule(studyPlanEvolves, mappedAudiences, university);
    }
}
