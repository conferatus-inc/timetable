package org.conferatus.timetable.backend.model.repos;

import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findSubjectById(Long id);

    Optional<Subject> findSubjectByName(String name);
}
