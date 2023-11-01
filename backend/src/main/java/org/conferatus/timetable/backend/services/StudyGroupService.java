package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.repos.StudyGroupRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    public Optional<StudyGroup> getGroup(String groupName) {
        return studyGroupRepository.findStudyGroupByGroupName(groupName);
    }
}
