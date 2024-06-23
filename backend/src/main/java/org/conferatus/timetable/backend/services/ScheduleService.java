package org.conferatus.timetable.backend.services;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GroupEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.SubjectEvolve;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleAlgorithmService algoService;
    private final AudienceService audienceService;
    private final SemesterPlanService semesterPlanService;

    public ScheduleAlgorithmService.StatusId generate(SemesterPlan sp) {
        List<GroupEvolve> groupEvolves = sp.studyGroups().stream().map(GroupEvolve::new).toList();
        List<SubjectEvolve> subjectEvolves = new ArrayList<>();

        // FIXME
//        for (SubjectPlan subjectPlan : sp.subjectPlans()) {
//            SubjectEvolve subjectEvolve;
//            if (Objects.requireNonNull(subjectPlan.subjectType()) == SubjectType.LECTURE) {
//                subjectEvolve = new SubjectEvolve(subjectPlan.id(),
//                        0, 1,
//                        Map.of(),
//                        new TeacherEvolve(
//                                subjectPlan.teachers().get(0),
//                                AudienceType.LECTURE
//                        ));
//            } else {
//                Map<Long, TeacherEvolve> groupNameToTeacher = new HashMap<>();
//
//                for (int i = 0; i < groupEvolves.size(); i++) {
//                    if (subjectPlan.teachers().isEmpty()) {
//                        throw new ServerException(HttpStatus.BAD_REQUEST,
//                                "There is no teachers for subject: " + subjectPlan);
//                    }
//                    groupNameToTeacher.put(groupEvolves.get(i).id(),
//                            new TeacherEvolve(subjectPlan.teachers().get(i % subjectPlan.teachers().size()),
//                                    AudienceType.PRACTICAL)
//                    );
//                }
//                subjectEvolve = new SubjectEvolve(subjectPlan.id(),
//                        Math.toIntExact(subjectPlan.timesPerWeek()), 0,
//                        groupNameToTeacher, null);
//            }
//            subjectEvolves.add(subjectEvolve);
//        }
//        List<StudyPlanEvolve> studyPlanEvolves = List.of(new StudyPlanEvolve(subjectEvolves, groupEvolves));
//
//        List<AudienceEvolve> mappedAudiences = audienceService.getAllAudiences()
//                .stream().map(AudienceEvolve::new).toList();
//        var task = algoService.createTaskSchedule(studyPlanEvolves, mappedAudiences);
//
//        return task;
        return null;
//        task.status().getResult().join();

//        algoService.getLastResult();
    }
}
