package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    Optional<StudyGroup> findStudyGroupByGroupName(String groupName);
}
