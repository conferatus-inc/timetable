package org.conferatus.timetable.backend.control;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.SemesterPlanDTO;
import org.conferatus.timetable.backend.control.dto.SubjectPlanDTO;
import org.conferatus.timetable.backend.model.enums.SubjectType;
import org.conferatus.timetable.backend.services.SemesterPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/semesterplans")
public class SemesterPlanController {
    private final SemesterPlanService semesterPlanService;

    @GetMapping
    public ResponseEntity<SemesterPlanDTO> getSemesterPlan(@RequestParam("id") Long id) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.getSemesterPlan(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterPlanDTO>> getAllSemesterPlans() {
        return ResponseEntity.ok(semesterPlanService.getAllSemesterPlans().stream()
                .map(SemesterPlanDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<SemesterPlanDTO> addSemesterPlan(@RequestParam("name") String semesterPlanName) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSemesterPlan(semesterPlanName)));
    }

    @DeleteMapping
    public ResponseEntity<SemesterPlanDTO> deleteSemesterPlan(@RequestParam("id") Long id) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSemesterPlan(id)));
    }

    @PostMapping("/subject")
    public ResponseEntity<SemesterPlanDTO> addSubjectPlan(
            @RequestParam("id") Long semesterId,
            @RequestParam("times") Long times,
            @RequestParam("subject_name") String subjectName,
            @RequestParam("subject_type") SubjectType subjectType
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSubjectPlan(
                semesterId, times, subjectName, subjectType
        )));
    }

    @DeleteMapping("/subject")
    public ResponseEntity<SemesterPlanDTO> deleteSubjectPlan(
            @RequestParam("id") Long semesterId,
            @RequestParam("subjectId") Long subjectId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSubjectPlan(
                semesterId, subjectId
        )));
    }

    @PostMapping("/subject/teacher")
    public ResponseEntity<SemesterPlanDTO> addSubjectTeacher(
            @RequestParam("id") Long semesterId,
            @RequestParam("subject_id") Long subjectId,
            @RequestParam("teacher_id") Long teacherId,
            @RequestParam("possibleTimes") Long possibleTimes
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.addSubjectTeacher(
                semesterId, subjectId, teacherId, possibleTimes
        )));
    }

    @DeleteMapping("/subject/teacher")
    public ResponseEntity<SemesterPlanDTO> deleteSubjectTeacher(
            @RequestParam("id") Long semesterId,
            @RequestParam("subject_id") Long subjectId,
            @RequestParam("teacher_id") Long teacherId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.deleteSubjectTeacher(
                semesterId, subjectId, teacherId
        )));
    }

    @GetMapping("/subject")
    public ResponseEntity<SubjectPlanDTO> getSubjectPlan(
            @RequestParam("id") Long semesterId,
            @RequestParam("subjectId") Long subjectId
    ) {
        return ResponseEntity.ok(new SubjectPlanDTO(semesterPlanService.getSubjectPlan(semesterId, subjectId)));
    }

    @PostMapping("/groups")
    public ResponseEntity<SemesterPlanDTO> linkGroup(
            @RequestParam("id") Long semesterId,
            @RequestParam("group_id") Long groupId
    ) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.linkGroup(semesterId, groupId)));
    }

    @GetMapping("/subject/all")
    public ResponseEntity<List<SubjectPlanDTO>> getAllSubjectPlans(@RequestParam("id") Long semesterId) {
        return ResponseEntity.ok(semesterPlanService.getAllSubjectPlans(semesterId)
                .stream().map(SubjectPlanDTO::new).toList());
    }
}
