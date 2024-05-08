package org.conferatus.timetable.backend.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.model.entity.Audience;
import org.conferatus.timetable.backend.model.repos.AudienceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AudienceService {
    private final AudienceRepository audienceRepository;

    private Audience getAudienceByIdOrThrow(Long id) {
        return audienceRepository.findAudienceById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Audience with id " + id + " does not exist"));
    }

    private Audience getAudienceByNameOrThrow(String name) {
        return audienceRepository.findAudienceByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Audience with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        audienceRepository.findAudienceByName(name).ifPresent(audience -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Audience with name " + name + " already exists");
        });
    }

    public Audience getAudience(Long id) {
        return getAudienceByIdOrThrow(id);
    }

    public Audience getAudience(String name) {
        return getAudienceByNameOrThrow(name);
    }

    public List<Audience> getAllAudiences() {
        return audienceRepository.findAll();
    }

    public Audience addAudience(String audienceName, AudienceType audienceType) {
        notExistsByNameOrThrow(audienceName);
        return audienceRepository.save(Audience.builder().name(audienceName).audienceType(audienceType).build());
    }

    public Audience updateAudience(String previousAudienceName, String newAudienceName) {
        var audience = getAudienceByNameOrThrow(previousAudienceName);
        if (newAudienceName.equals(previousAudienceName)) {
            return audience;
        }
        notExistsByNameOrThrow(newAudienceName);
        audience.setName(newAudienceName);
        return audienceRepository.save(audience);
    }

    public Audience deleteAudienceOrThrow(String audienceName) {
        var audience = getAudienceByNameOrThrow(audienceName);
        audienceRepository.delete(audience);
        return audience;
    }
}
