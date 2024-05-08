package org.conferatus.timetable.backend.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgoSchedule;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.control.dto.LessonDTO;
import org.conferatus.timetable.backend.control.dto.StudyGroupResponseDTO;
import org.conferatus.timetable.backend.control.dto.TableNasrano.Nasrano;
import org.conferatus.timetable.backend.control.dto.TimeListDTO;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.enums.TableTime;
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
    private final DtoConverter dtoConverter;

    private Nasrano currentSchedule;
    private List<Nasrano> currentSchedules;


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
    public ResponseEntity<Nasrano> chooseGeneratedTimetable(@RequestParam(required = false) Long taskId,
                                                            @RequestParam(defaultValue = "0") int chooseIndex) {
        List<AlgoSchedule> aboba = algoService.getLastResult().getResult().join();
        List<Nasrano> nasranos = new ArrayList<>();
        for (int i = 0; i < aboba.size(); i++) {
            var algoSchedule = aboba.get(i);
            nasranos.add(dtoConverter.convert(algoSchedule, i));
        }

        currentSchedule = nasranos.get(chooseIndex);

        return ResponseEntity.ok(nasranos.get(chooseIndex));
    }

    @GetMapping("generate/state")
    public ResponseEntity<AlgorithmStatus> getTaskState(@RequestParam(required = false) Long taskId) {
        return ResponseEntity.ok(algoService.getLastResult());
    }

    @GetMapping("generate/result")
    public ResponseEntity<List<Nasrano>> getTaskResult(@RequestParam(required = false) Long taskId) {
        List<AlgoSchedule> aboba = algoService.getLastResult().getResult().join();
        List<Nasrano> nasranos = new ArrayList<>();
        for (int i = 0; i < aboba.size(); i++) {
            var algoSchedule = aboba.get(i);
            nasranos.add(dtoConverter.convert(algoSchedule, i));
        }

        return ResponseEntity.ok(nasranos);
    }


    @GetMapping("/lessons/by_group")
    public ResponseEntity<TimeListDTO> getGroupLessons(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id
    ) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                228L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        for (LessonDTO lessonDTO : currentSchedule.timeListDTO().cells()) {
            for (StudyGroupResponseDTO group : lessonDTO.groups()) {
                if (id != null) {
                    if (Objects.equals(group.id(), id)) {
                        res.cells().add(lessonDTO);
                    }
                } else {
                    if (group.name().equals(name)) {
                        res.cells().add(lessonDTO);
                    }
                }
            }
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/lessons/by_teacher")
    public ResponseEntity<TimeListDTO> getTeacherLessons(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "id", required = false) Long id) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                228L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        for (LessonDTO lessonDTO : currentSchedule.timeListDTO().cells()) {
            if (id != null) {
                if (Objects.equals(lessonDTO.teacher().id(), id)) {
                    res.cells().add(lessonDTO);
                }
            } else {
                if (lessonDTO.teacher().name().equals(name)) {
                    res.cells().add(lessonDTO);
                }
            }
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/lessons/by_audience")
    public ResponseEntity<TimeListDTO> getAudienceLessons(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "id", required = false) Long id) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                228L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        for (LessonDTO lessonDTO : currentSchedule.timeListDTO().cells()) {
            if (id != null) {
                if (Objects.equals(lessonDTO.auditory().id(), id)) {
                    res.cells().add(lessonDTO);
                }
            } else {
                if (lessonDTO.auditory().name().equals(name)) {
                    res.cells().add(lessonDTO);
                }
            }
        }
        return ResponseEntity.ok(res);
    }
}
