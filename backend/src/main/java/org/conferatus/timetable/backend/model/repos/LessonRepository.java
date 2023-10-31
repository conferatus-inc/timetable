package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Long, Lesson> {
}
