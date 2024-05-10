package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.repository.SubjectPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectPlanService {
    private final SubjectPlanRepository subjectPlanRepository;

    public SubjectPlan getSubjectByIdOrThrow(Long id) {
        return subjectPlanRepository.findSubjectPlanById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SubjectPlan with id " + id + " does not exist"));
    }

    private SubjectPlan getSubjectByNameOrThrow(String name) {
        return subjectPlanRepository.findSubjectPlanByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SubjectPlan with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        if (subjectPlanRepository.findSubjectPlanByName(name).isPresent()) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "SubjectPlan with name " + name + " already exists");
        }
    }

    public SubjectPlan getSubject(Long id) {
        return getSubjectByIdOrThrow(id);
    }

    public SubjectPlan getSubject(String name) {
        return getSubjectByNameOrThrow(name);
    }

    public SubjectPlan addSubject(
            String subjectName, AudienceType subjectType,
            Long times
    ) {
        return subjectPlanRepository.save(
                new SubjectPlan().name(subjectName).subjectType(subjectType)
                        .timesPerWeek(times)
        );
    }
}
