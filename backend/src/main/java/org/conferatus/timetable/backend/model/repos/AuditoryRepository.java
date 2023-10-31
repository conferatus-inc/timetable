package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Auditory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoryRepository extends JpaRepository<Long, Auditory> {
}
