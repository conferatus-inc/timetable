package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Auditory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditoryRepository extends JpaRepository<Auditory, Long> {
    Optional<Auditory> findByName(String previousGroupName);
}
