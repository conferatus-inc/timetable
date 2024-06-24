package org.conferatus.timetable.backend.repository;

import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    boolean existsStudyGroupByName(String groupName);

    void deleteStudyGroupByName(String groupName);

    Optional<StudyGroup> findStudyGroupByName(String previousGroupName);

    Optional<StudyGroup> findStudyGroupById(Long id);
}
