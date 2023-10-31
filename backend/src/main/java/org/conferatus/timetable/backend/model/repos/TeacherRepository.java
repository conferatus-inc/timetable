package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Long, Subject> {
}
