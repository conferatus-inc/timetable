package org.conferatus.timetable.backend.control.dto;

import org.conferatus.timetable.backend.algorithm.constraints.PenaltyChecker;

import java.util.List;

public record TableNasrano(
        List<Nasrano> nasranos
) {


    public record Nasrano(
            TimeListDTO timeListDTOs,
            PenaltyChecker.CheckResult penalties
    ) {

    }
}
