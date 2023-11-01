package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherById(Long id);

    Optional<Teacher> findTeacherByName(String teacherName);
}
