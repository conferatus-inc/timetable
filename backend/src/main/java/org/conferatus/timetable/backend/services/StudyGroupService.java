package org.conferatus.timetable.backend.services;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.repository.StudyGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final UniversityService universityService;

    public StudyGroup getGroupByIdOrThrow(Long id) {
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
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Group with name " + name + " already exists");
        }
    }

    public StudyGroup getGroup(Long id) {
        return getGroupByIdOrThrow(id);
    }

    public StudyGroup getGroup(String name) {
        return getGroupByNameOrThrow(name);
    }

    public StudyGroup addGroup(University university, String groupName) {
        notExistsByNameOrThrow(groupName);
        var group = new StudyGroup();
        group.setName(groupName);
        group.setUniversity(university);
        university.studyGroups().add(group);
        var saved = studyGroupRepository.save(group);
        universityService.updateUniversity(university);
        return saved;
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

    public List<StudyGroup> getAllGroups(University university) {
        return studyGroupRepository.findAll()
                .stream().filter(it -> Objects.equals(it.getUniversity().id(), university.id())).toList();
    }
}
