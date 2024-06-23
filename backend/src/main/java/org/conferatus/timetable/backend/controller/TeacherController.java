package org.conferatus.timetable.backend.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.TeacherResponseDTO;
import org.conferatus.timetable.backend.dto.TeacherWishDto;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/by-id")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.getTeacher(user, id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<TeacherResponseDTO> getTeacherByName(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.getTeacher(user, name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(teacherService.getAllTeachers(user.getUniversity()).stream()
                .map(TeacherResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> addTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String teacherName
    ) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.addTeacher(user, teacherName)));
    }

    @PostMapping("/wishes")
    public ResponseEntity<TeacherResponseDTO> addTeacherWish(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String teacherName,
            @RequestBody TeacherWishDto teacherWish
    ) {
        return ResponseEntity.ok(new TeacherResponseDTO(teacherService.addTeacherWish(user, teacherName, teacherWish)));
    }

    @PutMapping
    public ResponseEntity<String> updateTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam("current") String previousTeacherName,
            @RequestParam("new") String newTeacherName
    ) {
        teacherService.updateTeacher(user, previousTeacherName, newTeacherName);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    public ResponseEntity<String> deleteTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam("name") String teacherName
    ) {
        teacherService.deleteTeacherOrThrow(user, teacherName);
        return ResponseEntity.ok().build();
    }
}
