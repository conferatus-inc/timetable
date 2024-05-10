package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerExceptions;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.repository.UniversityRepository;
import org.conferatus.timetable.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;

    public University createUniversity(String name) {
        return universityRepository.save(new University().name(name));
    }

    public University findById(Long universityId) {
        return universityRepository.findById(universityId).orElse(null);
    }

    public User linkUserToUniversity(Long universityId, Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            ServerExceptions.NOT_FOUND_EXCEPTION.moreInfo("User with id" + userId + "is not found").throwException();
        }
        var university = universityRepository.findById(universityId).orElse(null);
        if (university == null) {
            ServerExceptions.NOT_FOUND_EXCEPTION
                    .moreInfo("University with id" + universityId + "is not found").throwException();
        }
        user.setUniversity(university);
        System.out.println(user);
        return userRepository.save(user);
    }

    public University updateUniversity(University university) {
        return universityRepository.save(university);
    }
}
