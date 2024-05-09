package org.conferatus.timetable.backend.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.enums.SubjectType;
import org.conferatus.timetable.backend.model.entity.SemesterPlan;
import org.conferatus.timetable.backend.model.entity.SubjectPlan;
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

//    private void subjectTeacherNotExistsInSubjectPlanOrThrow(SubjectPlan subjectPlan, Long teacherId) {
//        if (subjectPlan.teachers()
//                .stream()
//                .anyMatch(teacher -> Objects.equals(teacher.teacher().getId(), teacherId))) {
//            throw new ServerException(HttpStatus.BAD_REQUEST,
//                    "Teacher with id " + teacherId + " already exists in subjectPlan " + subjectPlan.id());
//        }
//    }

//    private SubjectTeacher getSubjectTeacherInSubjectPlanOrThrow(SubjectPlan subjectPlan, Long teacherId) {
//        return subjectPlan.teachers()
//                .stream().filter(teacher -> Objects.equals(teacher.teacher().getId(), teacherId))
//                .findAny()
//                .orElseThrow(() -> new ServerException(HttpStatus.BAD_REQUEST,
//                        "Teacher with id " + teacherId + " does not exist in subjectPlan " + subjectPlan.id()));
//    }

    private void notExistsByNameOrThrow(String name) {
//        semesterPlanRepository.findSemesterPlanByName(name).ifPresent(semesterPlan -> {
//            throw new ServerException(HttpStatus.BAD_REQUEST,
//                    "SemesterPlan with name " + name + " already exists");
//        });
    }

    public SemesterPlan getSemesterPlan(Long id) {
        return getSemesterPlanByIdOrThrow(id);
    }

    public List<SemesterPlan> getAllSemesterPlans() {
        return semesterPlanRepository.findAll();
    }

    public SemesterPlan addSemesterPlan(String semesterPlanName) {
        notExistsByNameOrThrow(semesterPlanName);
        return semesterPlanRepository.save(
                new SemesterPlan()
                        .subjectPlans(Collections.emptyList()));
    }

    public SemesterPlan addSubjectPlan(Long semesterId, Long times, String subjectName, SubjectType subjectType) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        subjectPlanNotExistsInSemesterOrThrow(semesterPlan, subjectName);
        semesterPlan.subjectPlans().add(
                subjectPlanService.addSubject(subjectName, subjectType, times)
        );
        return semesterPlanRepository.save(semesterPlan);
    }

    public SubjectPlan getSubjectPlan(Long semesterId, Long subjectId) {
        return getSubjectPlanInSemesterPlanByIdOrThrow(getSemesterPlanByIdOrThrow(semesterId), subjectId);
    }

    public SemesterPlan addSubjectTeacher(Long semesterId, Long subjectId, Long teacherId, Long possibleTimes) {
        SemesterPlan semesterPlan = getSemesterPlanByIdOrThrow(semesterId);
        SubjectPlan subjectPlan = getSubjectPlanInSemesterPlanByIdOrThrow(semesterPlan, subjectId);
        // FIXME
//        subjectTeacherNotExistsInSubjectPlanOrThrow(subjectPlan, teacherId);
//        Teacher teacher = teacherService.getTeacher(teacherId);
//        subjectPlan.teachers().add(subjectTeacherService.addSubjectTeacher(teacher, possibleTimes));
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
