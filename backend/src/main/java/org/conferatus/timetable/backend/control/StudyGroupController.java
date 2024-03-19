package org.conferatus.timetable.backend.control;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.StudyGroupResponseDTO;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @GetMapping("/")
    public ResponseEntity<StudyGroupResponseDTO> getGroup(@Param("id") Long id) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.getGroup(id)));
    }

    @PostMapping("/")
    public ResponseEntity<String> addGroup(@Param("name") String groupName) {
        studyGroupService.addGroup(groupName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<String> updateGroup(@Param("current") String previousGroupName,
                                              @Param("new") String newGroupName) {
        studyGroupService.updateGroup(previousGroupName, newGroupName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteGroup(@Param("name") String groupName) {
        studyGroupService.deleteGroupOrThrow(groupName);
        return ResponseEntity.ok().build();
    }
}
