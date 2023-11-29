package org.conferatus.timetable.backend.control;

import java.util.List;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.AudienceDTO;
import org.conferatus.timetable.backend.model.AudienceType;
import org.conferatus.timetable.backend.services.AudienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/audience")
public class AudienceController {
    private final AudienceService audienceService;

    @GetMapping("/by-id")
    public ResponseEntity<AudienceDTO> getAudienceById(@Param("id") Long id) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.getAudience(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<AudienceDTO> getAudienceByName(@Param("name") String name) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.getAudience(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AudienceDTO>> getAllAudiences() {
        return ResponseEntity.ok(audienceService.getAllAudiences().stream()
                .map(AudienceDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<AudienceDTO> addAudience(
            @Param("name") String name,
            @Param("audience_type") AudienceType audienceType
    ) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.addAudience(name, audienceType)));
    }

    @PutMapping("/")
    public ResponseEntity<String> updateAudience(@Param("current") String previousAudienceName,
                                                 @Param("new") String newAudienceName) {
        audienceService.updateAudience(previousAudienceName, newAudienceName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteAudience(@Param("name") String audienceName) {
        audienceService.deleteAudienceOrThrow(audienceName);
        return ResponseEntity.ok().build();
    }
}
