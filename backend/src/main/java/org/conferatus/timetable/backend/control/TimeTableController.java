package org.conferatus.timetable.backend.control;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.control.dto.LessonDTO;
import org.conferatus.timetable.backend.control.dto.TimeListDTO;
import org.conferatus.timetable.backend.control.dto.TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Lesson;
import org.conferatus.timetable.backend.services.ScheduleAlgorithmService;
import org.conferatus.timetable.backend.services.ScheduleService;
import org.conferatus.timetable.backend.services.TimeTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/timetable")
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final ScheduleService scheduleService;
    private final ScheduleAlgorithmService algoService;


    private static void throwServerExceptionIfBothParametersAreNull(String name, Long id) {
        if (name == null && id == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "Id or name parameters should be presented");
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<Long> generateTimetable(@RequestParam Long semesterId) {
        var task = scheduleService.generate(semesterId);
        return ResponseEntity.ok(task.id());
    }

    @GetMapping("generate/choose")
    public ResponseEntity<List<TimeListDTO>> chooseGeneratedTimetable(@RequestParam Long generatedTimetableId) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("generate/state")
    public ResponseEntity<AlgorithmStatus> getTaskState(@RequestParam(required = false) Long taskId) {
        return ResponseEntity.ok(algoService.getLastResult());
    }

    @GetMapping("generate/result")
    public ResponseEntity<
            List<TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto.Nasrano>>
    getTaskResult(@RequestParam(required = false) Long taskId) {
        List<GeneticAlgorithmScheduler.AlgoSchedule> aboba = algoService.getLastResult().getResult().join();
        List<TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto.Nasrano>
                nasranos = TimeTableGeneratedResultCompletedFutureFinishedGeneratingAsynchronousWithWrongPenaltiesDto
                .sratVechno(aboba).nasranos();
        return ResponseEntity.ok(nasranos);
    }


    @GetMapping("/lessons/by_group")
    public ResponseEntity<TimeListDTO> getGroupLessons(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id
    ) {
        throwServerExceptionIfBothParametersAreNull(name, id);
        List<Lesson> lessons = id == null
                ? timeTableService.getLessonsByGroup(name)
                : timeTableService.getLessonsByGroup(id);
        return ResponseEntity.ok(
                new TimeListDTO(1L, List.of("Monday", "Tuesday"), 2, 5,
                        lessons.stream().map(LessonDTO::new).toList())
        );
    }

    @GetMapping("/lessons/by_teacher")
    public ResponseEntity<TimeListDTO> getTeacherLessons(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "id", required = false) Long id) {
        throwServerExceptionIfBothParametersAreNull(name, id);
        List<Lesson> lessons = id == null
                ? timeTableService.getLessonsByTeacher(name)
                : timeTableService.getLessonsByTeacher(id);
        return ResponseEntity.ok(
                new TimeListDTO(1L, List.of("Monday", "Tuesday"), 2, 5,
                        lessons.stream().map(LessonDTO::new).toList())
        );
    }

    @GetMapping("/lessons/by_audience")
    public ResponseEntity<TimeListDTO> getAudienceLessons(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "id", required = false) Long id) {
        throwServerExceptionIfBothParametersAreNull(name, id);
        List<Lesson> lessons = id == null
                ? timeTableService.getLessonsByAuditory(name)
                : timeTableService.getLessonsByAuditory(id);
        return ResponseEntity.ok(
                new TimeListDTO(1L, List.of("Monday", "Tuesday"), 2, 5,
                        lessons.stream().map(LessonDTO::new).toList())
        );
    }
}
