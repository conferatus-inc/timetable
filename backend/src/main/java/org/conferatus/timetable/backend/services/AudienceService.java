package org.conferatus.timetable.backend.services;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Audience;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.repository.AudienceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AudienceService {
    private final AudienceRepository audienceRepository;
    private final UniversityService universityService;

    public Audience getAudienceByIdOrThrow(Long id) {
        return audienceRepository.findAudienceById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Audience with id " + id + " does not exist"));
    }

    private Audience getAudienceByIdAndUserOrThrow(User user, Long id) {
        var audience = audienceRepository.findAudienceById(id);
        if (audience.isEmpty() || user != null && !user.checkUniversityAccess(audience.get().getUniversity().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Audience with id %s does dont exist within university %s",
                            id, user.getUniversity().id())
            );
        }
        return audience.get();
    }

    private Audience getAudienceByNameAndUserOrThrow(User user, String name) {
        var audience = audienceRepository.findAudienceByName(name);
        if (audience.isEmpty() || user != null && !user.checkUniversityAccess(audience.get().getUniversity().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Audience with name %s does dont exist within university %s",
                            name, user.getUniversity().id())
            );
        }
        return audience.get();
    }

    private void notExistsByNameOrThrow(String name) {
        audienceRepository.findAudienceByName(name).ifPresent(audience -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Audience with name " + name + " already exists");
        });
    }

    public Audience getAudience(User user, Long id) {
        return getAudienceByIdAndUserOrThrow(user, id);
    }

    public Audience getAudience(User user, String name) {
        return getAudienceByNameAndUserOrThrow(user, name);
    }

    public List<Audience> getAllAudiences(University university) {
        return audienceRepository.findAll().stream()
                .filter(it -> it.getUniversity().id() != null && Objects.equals(it.getUniversity().id(),
                        university.id())).toList();
    }

    public Audience addAudience(University university, String audienceName, AudienceType audienceType,
                                Long audienceGroupCapacity) {
        notExistsByNameOrThrow(audienceName);
        var auidence = new Audience();
        auidence.setName(audienceName);
        auidence.setAudienceType(audienceType);
        auidence.setUniversity(university);
        auidence.setAudienceGroupCapacity(audienceGroupCapacity);
        if (university != null) {
            university.audiences().add(auidence);
        }
        var result = audienceRepository.save(auidence);
        if (university != null) {
            universityService.updateUniversity(university);
        }
        return result;
    }

    public Audience updateAudience(User user, String previousAudienceName, String newAudienceName) {
        var audience = getAudienceByNameAndUserOrThrow(user, previousAudienceName);
        if (newAudienceName.equals(previousAudienceName)) {
            return audience;
        }
        notExistsByNameOrThrow(newAudienceName);
        audience.setName(newAudienceName);
        return audienceRepository.save(audience);
    }

    public Audience deleteAudienceOrThrow(User user, String audienceName) {
        var audience = getAudienceByNameAndUserOrThrow(user, audienceName);
        audienceRepository.delete(audience);
        return audience;
    }
}
