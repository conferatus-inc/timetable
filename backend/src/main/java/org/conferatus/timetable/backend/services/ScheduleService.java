package org.conferatus.timetable.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GroupEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.SubjectEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.TeacherEvolve;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.springframework.stereotype.Service;

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
            switch (subjectPlan.subjectType()) {

                case LECTURE -> subjectEvolve = new SubjectEvolve(subjectPlan.id().toString(),
                        0, 1,
                        Map.of(), new TeacherEvolve(subjectPlan.teachers().get(0),
                        AudienceType.LECTURE));
                default -> {
                    Map<String, TeacherEvolve> groupNameToTeacher = new HashMap<>();
                    for (int i = 0; i < groupEvolves.size(); i++) {
                        groupNameToTeacher.put(groupEvolves.get(i).id(),
                                new TeacherEvolve(subjectPlan.teachers().get(i % subjectPlan.teachers().size()),
                                        AudienceType.PRACTICAL)
                        );
                    }
                    subjectEvolve = new SubjectEvolve(subjectPlan.id().toString(),
                            Math.toIntExact(subjectPlan.times()), 0,
                            groupNameToTeacher, null);
                }
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
