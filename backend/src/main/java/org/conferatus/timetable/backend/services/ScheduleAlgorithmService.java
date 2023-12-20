package org.conferatus.timetable.backend.services;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyEnum;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.model.TableTime;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class ScheduleAlgorithmService {
    private final PenaltyChecker penaltyChecker = PenaltyChecker.newBuilder()
            .addPenalties(Arrays.stream(PenaltyEnum.values())
                    .toList()).build();

    private final Executor executor;
    private long counter = 0;

    private final Map<Long, AlgorithmStatus> taskIdToStatus;

    public ScheduleAlgorithmService() {
        executor = Executors.newFixedThreadPool(4);
        taskIdToStatus = new HashMap<>();
        TableTime.setCellsAmount(6);
        TableTime.setDaysAmount(6);
    }

    private StatusId algorithmCreateSchedule(List<StudyPlanEvolve> studyPlans,
                                             List<AudienceEvolve> audiences) {
        GeneticAlgorithmScheduler geneticAlgorithmScheduler = new GeneticAlgorithmScheduler();
        geneticAlgorithmScheduler.setPenalties(penaltyChecker);
        executor.execute(() -> geneticAlgorithmScheduler.algorithm(studyPlans, audiences));
        return new StatusId(counter++, geneticAlgorithmScheduler.algorithmStatus);
    }

    record StatusId(long id, AlgorithmStatus status) {
    }
}
