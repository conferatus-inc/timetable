package org.conferatus.timetable.backend.control;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.TeacherResponseDTO;
import org.conferatus.timetable.backend.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/")
    public ResponseEntity<TeacherResponseDTO> getTeacher(@Param("id") Long id) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.getTeacher(id)));
    }

    @PostMapping("/")
    public ResponseEntity<String> addTeacher(@Param("name") String teacherName) {
        teacherService.addTeacher(teacherName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<String> updateTeacher(@Param("current") String previousTeacherName,
                                                @Param("new") String newTeacherName) {
        teacherService.updateTeacher(previousTeacherName, newTeacherName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteTeacher(@Param("name") String teacherName) {
        teacherService.deleteTeacherOrThrow(teacherName);
        return ResponseEntity.ok().build();
    }
}
