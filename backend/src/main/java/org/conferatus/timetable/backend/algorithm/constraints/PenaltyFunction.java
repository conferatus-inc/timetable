package org.conferatus.timetable.backend.algorithm.constraints;

import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;

import java.util.function.Function;

public interface PenaltyFunction extends Function<GeneticAlgorithmScheduler.DataForConstraint, CalculateResult> {

}
