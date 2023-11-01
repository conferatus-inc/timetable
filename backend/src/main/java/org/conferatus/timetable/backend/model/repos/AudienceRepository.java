package org.conferatus.timetable.backend.model.repos;

import org.conferatus.timetable.backend.model.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {
    Optional<Audience> findByName(String previousGroupName);

    Optional<Audience> findAudienceById(Long id);

    Optional<Audience> findAudienceByName(String name);
}
