package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.model.TableTime;

public record TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto(
        List<Nasrano> nasranos
) {
    public static TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto sratVechno(
            List<GeneticAlgorithmScheduler.AlgoSchedule> aboba
    ) {
        return new TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto(
                aboba.stream().map(Nasrano::new).toList()
        );
    }

    public record Nasrano(
            TimeListDTO timeListDTOs,
            PenaltyChecker.CheckResult penalties
    ) {
        public Nasrano(GeneticAlgorithmScheduler.AlgoSchedule algoSchedule) {
            this(
                    new TimeListDTO(
                            228L,
                            List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                            TableTime.getDaysAmount(),
                            TableTime.getCellsAmount(),
                            algoSchedule.allLessons().stream().map(
                                    LessonDTO::new
                            ).toList()
                    ),
                    algoSchedule.checkResult()
            );
        }
    }
}
