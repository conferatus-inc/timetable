package org.conferatus.timetable.backend.model.repos;

import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectPlanRepository extends JpaRepository<SubjectPlan, Long> {

    boolean existsSubjectPlanByName(String subjectName);

    void deleteSubjectGroupByName(String subjectName);

    Optional<SubjectPlan> findSubjectPlanByName(String previousSubjectName);

    Optional<SubjectPlan> findSubjectPlanById(Long id);
}

