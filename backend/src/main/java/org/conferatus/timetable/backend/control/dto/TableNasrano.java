package org.conferatus.timetable.backend.control.dto;

import java.util.List;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;

public record TableNasrano(
        List<Nasrano> nasranos
) {


    public record Nasrano(
            TimeListDTO timeListDTO,
            PenaltyChecker.CheckResult penalties
    ) {

    }
}
