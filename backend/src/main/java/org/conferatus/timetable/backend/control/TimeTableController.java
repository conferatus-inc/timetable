package org.conferatus.timetable.backend.control;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.control.dto.LessonDTO;
import org.conferatus.timetable.backend.control.dto.TimeListDTO;
import org.conferatus.timetable.backend.model.entity.Lesson;
import org.conferatus.timetable.backend.services.TimeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/timetable")
public class TimeTableController {
    private final TimeTableService timeTableService;

    @GetMapping("/lessons/by_auditory")
    public ResponseEntity<TimeListDTO> getGroupLessons(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "id", required = false) Long id) {
        List<Lesson> lessons = id == null
                ? timeTableService.getLessonsByGroup(name)
                : timeTableService.getLessonsByGroup(id);
        return ResponseEntity.ok(
                new TimeListDTO(List.of("Monday", "Tuesday"), 2, 5,
                        lessons.stream().map(LessonDTO::new).toList())
        );
    }

    @GetMapping("/lessons/by_teacher")
    public ResponseEntity<TimeListDTO> getTeacherLessons(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "id", required = false) Long id) {
        List<Lesson> lessons = id == null
                ? timeTableService.getLessonsByTeacher(name)
                : timeTableService.getLessonsByTeacher(id);
        return ResponseEntity.ok(
                new TimeListDTO(List.of("Monday", "Tuesday"), 2, 5,
                        lessons.stream().map(LessonDTO::new).toList())
        );
    }

}
