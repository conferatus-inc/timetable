package org.conferatus.timetable.backend.control;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.model.entity.StudyGroup;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/table/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @GetMapping("/{groupName}")
    public ResponseEntity<StudyGroup> getGroup(@PathVariable String groupName) {
        return ResponseEntity.of(studyGroupService.getGroup(groupName));
    }
}
