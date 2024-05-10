package org.conferatus.timetable.backend.repository;

import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterPlanRepository extends JpaRepository<SemesterPlan, Long> {
    Optional<SemesterPlan> findSemesterPlanById(Long id);

    Optional<SemesterPlan> findSemesterPlanBySemesterName(String semesterName);
}
