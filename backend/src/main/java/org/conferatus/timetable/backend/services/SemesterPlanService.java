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

    private SemesterPlan getSemesterPlanByIdOrThrow(Long id) {
        return semesterPlanRepository.findSemesterPlanById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SemesterPlan with id " + id + " does not exist"));
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

//    private SubjectTeacher getSubjectTeacherInSubjectPlanOrThrow(SubjectPlan subjectPlan, Long teacherId) {
//        return subjectPlan.teachers()
//                .stream().filter(teacher -> Objects.equals(teacher.teacher().getId(), teacherId))
//                .findAny()
//                .orElseThrow(() -> new ServerException(HttpStatus.BAD_REQUEST,
//                        "Teacher with id " + teacherId + " does not exist in subjectPlan " + subjectPlan.id()));
//    }

    private void notExistsByNameOrThrow(String name) {
        semesterPlanRepository.findSemesterPlanBySemesterName(name).ifPresent(semesterPlan -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "SemesterPlan with name " + name + " already exists");
        });
    }

    public SemesterPlan getSemesterPlan(Long id) {
        return getSemesterPlanByIdOrThrow(id);
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

    public SubjectPlan getSubjectPlan(Long semesterId, Long subjectId) {
        return getSubjectPlanInSemesterPlanByIdOrThrow(getSemesterPlanByIdOrThrow(semesterId), subjectId);
    }

    public SemesterPlan addSubjectTeacher(Long semesterId, Long subjectId, Long teacherId) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        Teacher teacher = teacherService.getTeacher(teacherId);
        subjectPlan.teacher(teacher);
        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan linkGroup(Long semesterId, Long groupId) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);

        semesterPlan.studyGroups().add(studyGroupService.getGroupByIdOrThrow(groupId));

        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan deleteSemesterPlan(Long id) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(id);
        semesterPlanRepository.delete(semesterPlan);
        return semesterPlan;
    }

    public SemesterPlan deleteSubjectPlan(Long semesterId, Long subjectId) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        semesterPlan.subjectPlans().remove(subjectPlan);
        return semesterPlanRepository.save(semesterPlan);
    }

    public SemesterPlan deleteSubjectTeacher(Long semesterId, Long subjectId, Long teacherId) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        // FIXME
//        SubjectTeacher subjectTeacher = getSubjectTeacherInSubjectPlanOrThrow(subjectPlan, teacherId);
//        subjectPlan.teachers().remove(subjectTeacher);
        return semesterPlanRepository.save(semesterPlan);
    }

    public List<SubjectPlan> getAllSubjectPlans(Long semesterId) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        return semesterPlan.subjectPlans();
    }
}
