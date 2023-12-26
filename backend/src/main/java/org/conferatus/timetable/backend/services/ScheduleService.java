package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.*;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleAlgorithmService algoService;
    private final AudienceService audienceService;
    private final SemesterPlanService semesterPlanService;

    public ScheduleAlgorithmService.StatusId generate(Long semesterId) {
        SemesterPlan sp = semesterPlanService.getSemesterPlan(semesterId);


        List<GroupEvolve> groupEvolves = sp.studyGroups().stream().map(GroupEvolve::new).toList();
        List<SubjectEvolve> subjectEvolves = new ArrayList<>();

        for (SubjectPlan subjectPlan : sp.subjectPlans()) {
            SubjectEvolve subjectEvolve;
            if (Objects.requireNonNull(subjectPlan.subjectType()) == SubjectType.LECTURE) {
                subjectEvolve = new SubjectEvolve(subjectPlan.id(),
                        0, 1,
                        Map.of(), new TeacherEvolve(subjectPlan.teachers().get(0),
                        AudienceType.LECTURE));
            } else {
                Map<Long, TeacherEvolve> groupNameToTeacher = new HashMap<>();
                for (int i = 0; i < groupEvolves.size(); i++) {
                    groupNameToTeacher.put(groupEvolves.get(i).id(),
                            new TeacherEvolve(subjectPlan.teachers().get(i % subjectPlan.teachers().size()),
                                    AudienceType.PRACTICAL)
                    );
                }
                subjectEvolve = new SubjectEvolve(subjectPlan.id(),
                        Math.toIntExact(subjectPlan.times()), 0,
                        groupNameToTeacher, null);
            }
            subjectEvolves.add(subjectEvolve);
        }
        List<StudyPlanEvolve> studyPlanEvolves = List.of(new StudyPlanEvolve(subjectEvolves, groupEvolves));

        List<AudienceEvolve> mappedAudiences = audienceService.getAllAudiences()
                .stream().map(AudienceEvolve::new).toList();
        var task = algoService.createTaskSchedule(studyPlanEvolves, mappedAudiences);

        return task;
//        task.status().getResult().join();

//        algoService.getLastResult();
    }
}
