package org.conferatus.timetable.backend.controller;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.UniversityDto;
import org.conferatus.timetable.backend.dto.UserDto;
import org.conferatus.timetable.backend.services.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/universities")
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<UniversityDto> createUniversity(@RequestParam String name) {
        return ResponseEntity.ok(new UniversityDto(universityService.createUniversity(name)));
    }

    @PostMapping("/link")
    public ResponseEntity<UserDto> linkUserToUniversity(
            @RequestParam Long universityId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(universityService.linkUserToUniversity(universityId, userId).toUserDto());
    }
}
