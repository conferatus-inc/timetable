package org.conferatus.timetable.backend.repository;

import java.util.List;
import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherById(Long id);

    List<Teacher> findAll();

    Optional<Teacher> findByNameAndUniversity_Id(@NonNull String name, @NonNull Long id);
}
