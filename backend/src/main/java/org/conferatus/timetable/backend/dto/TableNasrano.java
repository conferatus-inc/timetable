package org.conferatus.timetable.backend.dto;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;

import java.util.List;

public record TableNasrano(
        List<Nasrano> nasranos
) {


    public record Nasrano(
            TimeListDTO timeListDTO,
            PenaltyChecker.CheckResult.CheckResultDTO penalties
    ) {

    }
}
