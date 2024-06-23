package org.conferatus.timetable.backend.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.StudyGroupResponseDTO;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.services.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @GetMapping("/by-id")
    public ResponseEntity<StudyGroupResponseDTO> getGroupById(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.getGroup(user, id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<StudyGroupResponseDTO> getGroupByName(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name
    ) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(studyGroupService.getGroup(user, name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudyGroupResponseDTO>> getAllGroups(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(studyGroupService.getAllGroups(user.getUniversity()).stream()
                .map(StudyGroupResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<StudyGroupResponseDTO> addGroup(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String groupName
    ) {
        return ResponseEntity.ok(new StudyGroupResponseDTO(
                studyGroupService.addGroup(user.getUniversity(), groupName)));
    }

    @PutMapping
    public ResponseEntity<String> updateGroup(
            @AuthenticationPrincipal User user,
            @RequestParam("current") String previousGroupName,
            @RequestParam("new") String newGroupName
    ) {
        studyGroupService.updateGroup(user, previousGroupName, newGroupName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    public ResponseEntity<String> deleteGroup(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String groupName
    ) {
        studyGroupService.deleteGroupOrThrow(user, groupName);
        return ResponseEntity.ok().build();
    }
}
