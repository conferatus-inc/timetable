package org.conferatus.timetable.backend.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.AudienceDTO;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.services.AudienceService;
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
@RequestMapping("/api/v1/admin/audience")
public class AudienceController {
    private final AudienceService audienceService;

    @GetMapping("/by-id")
    public ResponseEntity<AudienceDTO> getAudienceById(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.getAudience(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<AudienceDTO> getAudienceByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.getAudience(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AudienceDTO>> getAllAudiences(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(audienceService.getAllAudiences(user.getUniversity()).stream()
                .map(AudienceDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<AudienceDTO> addAudience(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("audience_type") AudienceType audienceType,
            @RequestParam("audience_group_capacity") Long audienceGroupCapacity

    ) {
        return ResponseEntity.ok(new AudienceDTO(
                audienceService.addAudience(user.getUniversity(), name, audienceType, audienceGroupCapacity)
        ));
    }

    @PutMapping
    public ResponseEntity<String> updateAudience(@RequestParam("current") String previousAudienceName,
                                                 @RequestParam("new") String newAudienceName) {
        audienceService.updateAudience(previousAudienceName, newAudienceName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAudience(@RequestParam("name") String audienceName) {
        audienceService.deleteAudienceOrThrow(audienceName);
        return ResponseEntity.ok().build();
    }
}
