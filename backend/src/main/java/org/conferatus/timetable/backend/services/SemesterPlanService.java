package org.conferatus.timetable.backend.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.exception.ServerExceptions;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.repository.SemesterPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterPlanService {
    private final SemesterPlanRepository semesterPlanRepository;
    private final TeacherService teacherService;
    private final StudyGroupService studyGroupService;
    private final SubjectPlanService subjectPlanService;
    private final UniversityService universityService;

    public SemesterPlan getSemesterPlanByIdOrThrow(Long id) {
        return semesterPlanRepository.findSemesterPlanById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SemesterPlan with id " + id + " does not exist"));
    }

    public SemesterPlan getSemesterPlanByUserAndIdOrThrow(User user, Long id) {
        var semesterPlan = semesterPlanRepository.findSemesterPlanById(id);
        if (semesterPlan.isEmpty() || !user.checkUniversityAccess(semesterPlan.get().university().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("SemesterPlan with id %s in university %s does not exist",
                            id, user.getUniversity().id()));
        }
        return semesterPlan.get();
    }

    private SubjectPlan getSubjectPlanInSemesterPlanByIdOrThrow(SemesterPlan semesterPlan, Long subjectId) {
        return semesterPlan.subjectPlans()
                .stream().filter(subjectPlan -> Objects.equals(subjectPlan.id(), subjectId))
                .findFirst()
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Subject with id " + subjectId + " does not exist in semester " + semesterPlan.id()));
    }

    private void subjectPlanNotExistsInSemesterOrThrow(SemesterPlan semesterPlan, String subjectName) {
        if (semesterPlan.subjectPlans()
                .stream()
                .anyMatch(subjectPlan -> Objects.equals(subjectPlan.name(), subjectName))) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "SubjectPlan with name " + subjectName + " already exists in semester " + semesterPlan.id());
        }
    }

    private void notExistsByNameOrThrow(String name) {
        semesterPlanRepository.findSemesterPlanBySemesterName(name).ifPresent(semesterPlan -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "SemesterPlan with name " + name + " already exists");
        });
    }

    public SemesterPlan getSemesterPlan(User user, Long id) {
        return getSemesterPlanByUserAndIdOrThrow(user, id);
    }

    public List<SemesterPlan> getAllSemesterPlans(University university) {
        return semesterPlanRepository.findAll()
                .stream().filter(it -> Objects.equals(it.university().id(), university.id())).toList();
    }

    public SemesterPlan addSemesterPlan(University university, String semesterPlanName) {
        notExistsByNameOrThrow(semesterPlanName);
        var semPlan = new SemesterPlan();
        semPlan.subjectPlans(Collections.emptyList());
        semPlan.university(university);
        var saved = semesterPlanRepository.save(semPlan);
        universityService.updateUniversity(university);
        return saved;
    }

    public SemesterPlan addSubjectPlan(University university, Long semesterId, Long times, String subjectName,
                                       AudienceType subjectType) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        if (!Objects.equals(semesterPlan.university().id(), university.id())) {
            ServerExceptions.NOT_FOUND_EXCEPTION
                    .moreInfo("You don't have access to university" + semesterPlan.university().id()).throwException();
        }
        subjectPlanNotExistsInSemesterOrThrow(semesterPlan, subjectName);
        semesterPlan.subjectPlans().add(
                subjectPlanService.addSubject(subjectName, subjectType, times)
        );
        return semesterPlanRepository.save(semesterPlan);
    }

    public SubjectPlan getSubjectPlan(User user, Long semesterId, Long subjectId) {
        return getSubjectPlanInSemesterPlanByIdOrThrow(getSemesterPlanByUserAndIdOrThrow(user, semesterId), subjectId);
    }

    public SemesterPlan addSubjectTeacher(User user, Long semesterId, Long subjectId, Long teacherId) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        Teacher teacher = teacherService.getTeacher(user, teacherId);
        subjectPlan.teacher(teacher);
        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan linkGroup(User user, Long semesterId, Long groupId) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, semesterId);

        semesterPlan.studyGroups().add(studyGroupService.getGroupByIdOrThrow(groupId));

        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan deleteSemesterPlan(User user, Long id) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, id);
        semesterPlanRepository.delete(semesterPlan);
        return semesterPlan;
    }

    public SemesterPlan deleteSubjectPlan(User user, Long semesterId, Long subjectId) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        semesterPlan.subjectPlans().remove(subjectPlan);
        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan deleteSubjectTeacher(User user, Long semesterId, Long subjectId) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        subjectPlan.teacher(null);
        return semesterPlanRepository.save(semesterPlan);
    }

    public List<SubjectPlan> getAllSubjectPlans(User user, Long semesterId) {
        SemesterPlan semesterPlan = getSemesterPlanByUserAndIdOrThrow(user, semesterId);
        return semesterPlan.subjectPlans();
    }
}
