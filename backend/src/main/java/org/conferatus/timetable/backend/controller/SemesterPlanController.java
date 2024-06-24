package org.conferatus.timetable.backend.controller;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.SemesterPlanDTO;
import org.conferatus.timetable.backend.dto.SubjectPlanDTO;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.AudienceType;
import org.conferatus.timetable.backend.services.SemesterPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/semesterplans")
public class SemesterPlanController {
    private final SemesterPlanService semesterPlanService;

    @GetMapping
    public ResponseEntity<SemesterPlanDTO> getSemesterPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.getSemesterPlan(user, id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterPlanDTO>> getAllSemesterPlans(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(semesterPlanService.getAllSemesterPlans(user.getUniversity()).stream()
                .map(SemesterPlanDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<SemesterPlanDTO> addSemesterPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String semesterPlanName
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(
                semesterPlanService.addSemesterPlan(user.getUniversity(), semesterPlanName)));
    }

    @DeleteMapping
    public ResponseEntity<SemesterPlanDTO> deleteSemesterPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSemesterPlan(user, id)));
    }

    @PostMapping("/subject")
    public ResponseEntity<SemesterPlanDTO> addSubjectPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("times") Long times,
            @RequestParam("subject_name") String subjectName,
            @RequestParam("subject_type") AudienceType subjectType
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSubjectPlan(
                user.getUniversity(), semesterId, times, subjectName, subjectType
        )));
    }

    @DeleteMapping("/subject")
    public ResponseEntity<SemesterPlanDTO> deleteSubjectPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("subjectId") Long subjectId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSubjectPlan(
                user, semesterId, subjectId
        )));
    }

    @PostMapping("/subject/group")
    public ResponseEntity<SemesterPlanDTO> addSubjectGroup(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("subject_id") Long subjectId,
            @RequestParam("group_id") Long groupId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSubjectGroup(
                user, semesterId, subjectId, groupId
        )));
    }


    @PostMapping("/subject/teacher")
    public ResponseEntity<SemesterPlanDTO> addSubjectTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("subject_id") Long subjectId,
            @RequestParam("teacher_id") Long teacherId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSubjectTeacher(
                user, semesterId, subjectId, teacherId
        )));
    }

    @DeleteMapping("/subject/teacher")
    public ResponseEntity<SemesterPlanDTO> deleteSubjectTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("subject_id") Long subjectId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSubjectTeacher(
                user, semesterId, subjectId
        )));
    }

    @GetMapping("/subject")
    public ResponseEntity<SubjectPlanDTO> getSubjectPlan(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("subjectId") Long subjectId
    ) {
        return ResponseEntity.ok(new SubjectPlanDTO(semesterPlanService.getSubjectPlan(user, semesterId, subjectId)));
    }

    @PostMapping("/groups")
    public ResponseEntity<SemesterPlanDTO> linkGroup(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId,
            @RequestParam("group_id") Long groupId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.linkGroup(user, semesterId, groupId)));
    }

    @GetMapping("/subject/all")
    public ResponseEntity<List<SubjectPlanDTO>> getAllSubjectPlans(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long semesterId
    ) {
        return ResponseEntity.ok(semesterPlanService.getAllSubjectPlans(user, semesterId)
                .stream().map(SubjectPlanDTO::new).toList());
    }
}
