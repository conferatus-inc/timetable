package org.conferatus.timetable.backend.control;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.AudienceDTO;
import org.conferatus.timetable.backend.services.AudienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/audience")
public class AudienceController {
    private final AudienceService audienceService;

    @GetMapping("/")
    public ResponseEntity<AudienceDTO> getAudience(@Param("id") Long id) {
        return ResponseEntity.ok(new AudienceDTO(audienceService.getAudience(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AudienceDTO>> getAllAudiences() {
        return ResponseEntity.ok(audienceService.getAllAudiences().stream()
                .map(AudienceDTO::new).toList());
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
