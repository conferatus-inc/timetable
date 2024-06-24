package org.conferatus.timetable.backend.repository;

import org.conferatus.timetable.backend.model.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
