package org.conferatus.timetable.backend.services;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.constraints.PenaltyEnum;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.model.TableTime;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleAlgorithmService {
    private final GeneticAlgorithmScheduler geneticAlgorithmScheduler;
    private final PenaltyChecker penaltyChecker = PenaltyChecker.newBuilder()
            .addPenalties(Arrays.stream(PenaltyEnum.values())
                    .toList()).build();

    public ScheduleAlgorithmService() {
        this.geneticAlgorithmScheduler = new GeneticAlgorithmScheduler();
        geneticAlgorithmScheduler.setPenalties(penaltyChecker);
        TableTime.setCellsAmount(6);
        TableTime.setDaysAmount(6);
    }

    private List<GeneticAlgorithmScheduler.AlgoSchedule> algorithmCreateSchedule(List<StudyPlanEvolve> studyPlans,
                                                                                 List<AudienceEvolve> audiences) {
        return geneticAlgorithmScheduler.algorithm(studyPlans, audiences);
    }
}
