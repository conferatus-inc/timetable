package org.conferatus.timetable.backend.services;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.entity.User;
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

    public StudyGroup getGroupByUserAndIdOrThrow(User user, Long id) {
        var group = studyGroupRepository.findStudyGroupById(id);
        if (group.isEmpty() || !user.checkUniversityAccess(group.get().getUniversity().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Group with id %s in university %s does not exist",
                            id, user.getUniversity().id()));
        }
        return group.get();
    }

    private StudyGroup getGroupByUserAndNameOrThrow(User user, String name) {
        var group = studyGroupRepository.findStudyGroupByName(name);
        if (group.isEmpty() || !user.checkUniversityAccess(group.get().getUniversity().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Group with name %s in university %s does not exist",
                            name, user.getUniversity().id()));
        }
        return group.get();
    }

    private void notExistsByNameOrThrow(String name) {
        if (studyGroupRepository.findStudyGroupByName(name).isPresent()) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Group with name " + name + " already exists");
        }
    }

    public StudyGroup getGroup(User user, Long id) {
        return getGroupByUserAndIdOrThrow(user, id);
    }

    public StudyGroup getGroup(User user, String name) {
        return getGroupByUserAndNameOrThrow(user, name);
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

    public StudyGroup updateGroup(User user, String previousGroupName, String newGroupName) {
        var group = getGroupByUserAndNameOrThrow(user, previousGroupName);
        if (newGroupName.equals(previousGroupName)) {
            return group;
        }
        notExistsByNameOrThrow(newGroupName);
        group.setName(newGroupName);
        return studyGroupRepository.save(group);
    }

    public StudyGroup deleteGroupOrThrow(User user, String groupName) {
        var group = getGroupByUserAndNameOrThrow(user, groupName);
        studyGroupRepository.delete(group);
        return group;
    }

    public List<StudyGroup> getAllGroups(University university) {
        return studyGroupRepository.findAll()
                .stream().filter(it -> it.getUniversity().id() != null && Objects.equals(it.getUniversity().id(),
                        university.id())).toList();
    }
}
