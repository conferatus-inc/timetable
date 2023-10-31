package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyPlanRepository extends JpaRepository<Long, StudyPlan> {
}
