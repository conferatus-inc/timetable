package org.conferatus.timetable.backend.control;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @PostMapping("/{groupName}")
    public ResponseEntity<String> addGroup(@PathVariable String groupName) {
        studyGroupService.addGroup(groupName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateGroup(@Param("current") String previousGroupName,
                                              @Param("new") String newGroupName) {
        studyGroupService.updateGroup(previousGroupName, newGroupName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName) {
        studyGroupService.deleteGroupOrThrow(groupName);
        return ResponseEntity.ok().build();
    }
}
