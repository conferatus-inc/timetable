package org.conferatus.timetable.backend.control;

import java.util.List;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.SemesterPlanDTO;
import org.conferatus.timetable.backend.control.dto.SubjectDTO;
import org.conferatus.timetable.backend.services.SemesterPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/semester-plan")
public class SemesterPlanController {
    private final SemesterPlanService semesterPlanService;

    @GetMapping("/")
    public ResponseEntity<SemesterPlanDTO> getSemesterPlan(@Param("id") Long id) {
        return ResponseEntity.ok(new SemesterPlanDTO(semesterPlanService.getSemesterPlan(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterPlanDTO>> getAllSemesterPlans() {
        return ResponseEntity.ok(semesterPlanService.getAllSemesterPlans().stream()
                .map(SemesterPlanDTO::new).toList());
    }
}
