package org.conferatus.timetable.backend.services;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyEnum;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.enums.TableTime;
import org.conferatus.timetable.backend.util.ThreadPoolProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ScheduleAlgorithmService {
    private final PenaltyChecker penaltyChecker = PenaltyChecker.newBuilder()
            .addPenalties(Arrays.stream(PenaltyEnum.values())
                    .toList()).build();

    private final Executor executor;
    private final AtomicLong counter = new AtomicLong(0);

    private final Map<Long, AlgorithmStatus> taskIdToStatus;
    private final Map<Long, Long> lastUniversityTaskId = new HashMap<>();

    public ScheduleAlgorithmService(ThreadPoolProvider threadPoolProvider) {
        executor = threadPoolProvider.highPriority();
        taskIdToStatus = new HashMap<>(100);
        TableTime.setCellsAmount(6);
        TableTime.setDaysAmount(6);
    }

    private AlgorithmStatus createAlgorithmSchedule(List<StudyPlanEvolve> studyPlans,
                                                    List<AudienceEvolve> audiences) {
        GeneticAlgorithmScheduler geneticAlgorithmScheduler = new GeneticAlgorithmScheduler();
        geneticAlgorithmScheduler.setPenalties(penaltyChecker);
        var status = geneticAlgorithmScheduler.asyncStart(studyPlans, audiences, executor);
        return status;
    }

    public StatusId createTaskSchedule(List<StudyPlanEvolve> studyPlans,
                                       List<AudienceEvolve> audiences,
                                       University university) {
        var statusWithId = new StatusId(counter.incrementAndGet(), createAlgorithmSchedule(studyPlans, audiences));
        taskIdToStatus.put(statusWithId.id, statusWithId.status);
        lastUniversityTaskId.put(university.id(), statusWithId.id);
        return statusWithId;
    }

    public AlgorithmStatus getTaskStatus(Long taskId) {
        return taskIdToStatus.get(taskId);
    }

    public void completeTask(Long taskId) {
        taskIdToStatus.remove(taskId);
    }

    public record StatusId(long id, AlgorithmStatus status) {
    }

    public AlgorithmStatus getLastResult(University university) {
        return taskIdToStatus.get(lastUniversityTaskId.get(university.id()));
    }
}