package org.conferatus.timetable.backend.control;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.services.TeacherToSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/teacher_to_subject")
public class TeacherToSubjectController {
    private final TeacherToSubjectService teacherToSubjectService;

    @PostMapping("/")
    public ResponseEntity<String> addTeacherSubjectLink(
            @Param("teacher_id") Long teacherId,
            @Param("subject_id") Long subjectId
    ) {
        teacherToSubjectService.addTeacherSubjectLink(teacherId, subjectId);
        return ResponseEntity.ok().build();
    }
}