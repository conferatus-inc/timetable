package org.conferatus.timetable.backend.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.repos.SemesterPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterPlanService {
    private final SemesterPlanRepository semesterPlanRepository;

    private SemesterPlan getSemesterPlanByIdOrThrow(Long id) {
        return semesterPlanRepository.findSemesterPlanById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Subject with id " + id + " does not exist"));
    }

    public SemesterPlan getSemesterPlan(Long id) {
        return getSemesterPlanByIdOrThrow(id);
    }

    public List<SemesterPlan> getAllSemesterPlans() {
        return semesterPlanRepository.findAll();
    }
}
