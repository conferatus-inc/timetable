package org.conferatus.timetable.backend.control;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/table/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @PostMapping("/{groupName}")
    public ResponseEntity<String> addGroup(@PathVariable String groupName) {
        if (studyGroupService.addGroup(groupName)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Group with name " + groupName + " already exists");
        }
    }
}
