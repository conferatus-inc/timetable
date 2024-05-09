package org.conferatus.timetable.backend.controller;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.UniversityDto;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.services.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/universities")
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<UniversityDto> createUniversity(@RequestBody String name) {
        return ResponseEntity.ok(new UniversityDto(universityService.createUniversity(name)));
    }

    @PostMapping("/link")
    public ResponseEntity<User> linkUserToUniversity(
            @RequestHeader("university_id") Long universityId,
            @RequestBody Long userId
    ) {
        return ResponseEntity.ok(universityService.linkUserToUniversity(universityId, userId));
    }
}
