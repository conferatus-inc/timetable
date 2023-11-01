package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.repos.StudyGroupRepository;
import org.springframework.stereotype.Service;

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

    public boolean updateGroup(String groupName) {
        if (!studyGroupRepository.existsStudyGroupByName(groupName)) {
            return false;
        } else {
            studyGroupRepository.save(new StudyGroup(null, groupName, null)); // TODO constructor
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
