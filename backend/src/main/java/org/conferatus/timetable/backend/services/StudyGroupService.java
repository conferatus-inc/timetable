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

    public boolean addGroup(String groupName) {
        if (studyGroupRepository.existsStudyGroupByName(groupName)) {
            return false;
        } else {
            studyGroupRepository.save(new StudyGroup(null, groupName, null)); // TODO constructor
            return true;
        }
    }

    public boolean updateGroup(String previousGroupName, String newGroupName) {
        if (!studyGroupRepository.existsStudyGroupByName(previousGroupName)) {
            return false;
        } else {
            Optional<StudyGroup> studyGroup = studyGroupRepository.findStudyGroupByName(previousGroupName);
            studyGroup.ifPresent(value -> value.setName(newGroupName)); // FIXME
            studyGroup.ifPresent(studyGroupRepository::save); // FIXME
            return true;
        }
    }

    public boolean deleteGroup(String groupName) {
        if (!studyGroupRepository.existsStudyGroupByName(groupName)) {
            return false;
        } else {
            studyGroupRepository.deleteStudyGroupByName(groupName);
            return true;
        }
    }
}
