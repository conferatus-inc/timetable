package org.conferatus.timetable.backend.control;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @PostMapping("/{groupName}")
    public ResponseEntity<String> addGroup(@PathVariable String groupName) {
        if (studyGroupService.addGroup(groupName)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ServerException(HttpStatus.BAD_REQUEST, "Group with name " + groupName + " already exists");
        }
    }

    @PutMapping("/{previousGroupName}/{newGroupName}")
    public ResponseEntity<String> updateGroup(@PathVariable String previousGroupName, @PathVariable String newGroupName) {
        if (studyGroupService.updateGroup(previousGroupName, newGroupName)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ServerException(HttpStatus.NOT_FOUND, "Group with name " + previousGroupName + " does not exist");
        }
    }

    @DeleteMapping("/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName) {
        if (studyGroupService.deleteGroup(groupName)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ServerException(HttpStatus.NOT_FOUND, "Group with name " + groupName + " does not exist");
        }
    }
}
