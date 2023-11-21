package org.conferatus.timetable.backend.services;

import org.conferatus.timetable.backend.algorithm.constraints.Penalties;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleAlgorithmService {
    private final GeneticScheduler geneticScheduler;
    private final int populationSize;
    private final int times;

    public ScheduleAlgorithmService() {
        this.geneticScheduler = new GeneticScheduler();
        geneticScheduler.setPenalties(Arrays.stream(
                        Penalties.values())
                .map(Penalties::getPenaltyFunction).toList());
        populationSize = 400;
        times = 6 * 7;
    }

    private List<List<LessonWithTime>> algorithmCreateSchedule(List<StudyPlanEvolve> studyPlans,
                                                               List<AudienceEvolve> audiences) {
        return geneticScheduler.algorithm(studyPlans, audiences, times, populationSize);
    }
}
