package org.conferatus.timetable.backend.control;

import java.util.List;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.SubjectDTO;
import org.conferatus.timetable.backend.model.SubjectType;
import org.conferatus.timetable.backend.services.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/subject")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/by-id")
    public ResponseEntity<SubjectDTO> getSubjectById(@Param("id") Long id) {
        return ResponseEntity.ok(new SubjectDTO(subjectService.getSubject(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<SubjectDTO> getSubjectByName(@Param("name") String name) {
        return ResponseEntity.ok(new SubjectDTO(subjectService.getSubject(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects().stream()
                .map(SubjectDTO::new).toList());
    }

    @PostMapping("/")
    public ResponseEntity<SubjectDTO> addSubject(
            @Param("name") String subjectName,
            @Param("subject_type") SubjectType subjectType
    ) {
        return ResponseEntity.ok(new SubjectDTO(subjectService.addSubject(subjectName, subjectType)));
    }

    @PutMapping("/")
    public ResponseEntity<String> updateSubject(@Param("current") String previousSubjectName,
                                                @Param("new") String newSubjectName) {
        subjectService.updateSubject(previousSubjectName, newSubjectName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteSubject(@Param("name") String SubjectName) {
        subjectService.deleteSubjectOrThrow(SubjectName);
        return ResponseEntity.ok().build();
    }
}
