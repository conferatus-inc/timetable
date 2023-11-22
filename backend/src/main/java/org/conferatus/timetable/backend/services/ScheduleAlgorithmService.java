package org.conferatus.timetable.backend.services;

import org.conferatus.timetable.backend.algorithm.constraints.Penalties;
import org.conferatus.timetable.backend.algorithm.scheduling.AudienceEvolve;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.algorithm.scheduling.StudyPlanEvolve;
import org.conferatus.timetable.backend.model.TableTime;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleAlgorithmService {
    private final GeneticScheduler geneticScheduler;
    private final int populationSize;

    public ScheduleAlgorithmService() {
        this.geneticScheduler = new GeneticScheduler();
        geneticScheduler.setPenalties(Arrays.stream(
                        Penalties.values())
                .map(Penalties::getPenaltyFunction).toList());
        populationSize = 400;
        TableTime.setCellsAmount(6);
        TableTime.setDaysAmount(6);
    }

    private List<List<LessonWithTime>> algorithmCreateSchedule(List<StudyPlanEvolve> studyPlans,
                                                               List<AudienceEvolve> audiences) {
        return geneticScheduler.algorithm(studyPlans, audiences, populationSize);
    }
}
