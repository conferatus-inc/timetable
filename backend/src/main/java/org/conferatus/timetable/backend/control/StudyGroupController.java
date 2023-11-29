package org.conferatus.timetable.backend.control;

import java.util.List;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.StudyGroupResponseDTO;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @GetMapping("/by-id")
    public ResponseEntity<StudyGroupResponseDTO> getGroupById(@Param("id") Long id) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.getGroup(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<StudyGroupResponseDTO> getGroupByName(@Param("name") String name) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.getGroup(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudyGroupResponseDTO>> getAllGroups() {
        return ResponseEntity.ok(studyGroupService.getAllGroups().stream()
                .map(StudyGroupResponseDTO::new).toList());
    }

    @PostMapping("/")
    public ResponseEntity<StudyGroupResponseDTO> addGroup(@Param("name") String groupName) {
        studyGroupService.addGroup(groupName);
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.addGroup(groupName)));
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
