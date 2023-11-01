package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    boolean existsStudyGroupByName(String groupName);

    void deleteStudyGroupByName(String groupName);
}
