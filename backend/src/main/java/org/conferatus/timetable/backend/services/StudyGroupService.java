package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.repos.StudyGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    private StudyGroup getGroupByIdOrThrow(Long id) {
        return studyGroupRepository.findStudyGroupById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Group with id " + id + " does not exist"));
    }

    private StudyGroup getGroupByNameOrThrow(String name) {
        return studyGroupRepository.findStudyGroupByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Group with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        if (studyGroupRepository.findStudyGroupByName(name).isPresent()) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    "Group with name " + name + " does not exist");
        }
    }

    public StudyGroup addGroup(String groupName) {
        notExistsByNameOrThrow(groupName);
        return studyGroupRepository.save(StudyGroup.builder().name(groupName).build());
    }

    public StudyGroup updateGroup(String previousGroupName, String newGroupName) {
        var group = getGroupByNameOrThrow(previousGroupName);
        if (newGroupName.equals(previousGroupName)) {
            return group;
        }
        notExistsByNameOrThrow(newGroupName);
        group.setName(newGroupName);
        return studyGroupRepository.save(group);
    }

    public StudyGroup deleteGroupOrThrow(String groupName) {
        var group = getGroupByNameOrThrow(groupName);
        studyGroupRepository.delete(group);
        return group;
    }

    public StudyGroup getGroup(Long id) {
        return getGroupByIdOrThrow(id);
    }
}
