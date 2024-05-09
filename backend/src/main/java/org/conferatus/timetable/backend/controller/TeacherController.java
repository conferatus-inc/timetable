package org.conferatus.timetable.backend.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.TeacherResponseDTO;
import org.conferatus.timetable.backend.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/by-id")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.getTeacher(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<TeacherResponseDTO> getTeacherByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.getTeacher(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers().stream()
                .map(TeacherResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> addTeacher(@RequestParam("name") String teacherName) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.addTeacher(teacherName)));
    }

    @PutMapping
    public ResponseEntity<String> updateTeacher(@RequestParam("current") String previousTeacherName,
                                                @RequestParam("new") String newTeacherName) {
        teacherService.updateTeacher(previousTeacherName, newTeacherName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    public ResponseEntity<String> deleteTeacher(@RequestParam("name") String teacherName) {
        teacherService.deleteTeacherOrThrow(teacherName);
        return ResponseEntity.ok().build();
    }
}
