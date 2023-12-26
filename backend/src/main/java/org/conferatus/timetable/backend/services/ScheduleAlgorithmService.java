package org.conferatus.timetable.backend.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyEnum;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.model.TableTime;
import org.springframework.stereotype.Service;

@Service
public class ScheduleAlgorithmService {
    private final PenaltyChecker penaltyChecker = PenaltyChecker.newBuilder()
            .addPenalties(Arrays.stream(PenaltyEnum.values())
                    .toList()).build();

    private final Executor executor;
    private final AtomicLong counter = new AtomicLong(0);

    private final Map<Long, AlgorithmStatus> taskIdToStatus;

    public ScheduleAlgorithmService() {
        executor = Executors.newFixedThreadPool(4);
        taskIdToStatus = new HashMap<>();
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
                                        List<AudienceEvolve> audiences) {
        var statusWithId = new StatusId(counter.incrementAndGet(), createAlgorithmSchedule(studyPlans, audiences));
        taskIdToStatus.put(statusWithId.id, statusWithId.status);
        return statusWithId;
    }

    public AlgorithmStatus getTaskStatus(Long taskId) {
        return taskIdToStatus.get(taskId);
    }

    public void completeTask(Long taskId) {
        taskIdToStatus.remove(taskId);
    }

    record StatusId(long id, AlgorithmStatus status) {
    }

    public AlgorithmStatus getLastResult() {
        return taskIdToStatus.get(counter.get());
    }
}
