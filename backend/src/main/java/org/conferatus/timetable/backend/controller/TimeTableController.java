package org.conferatus.timetable.backend.controller;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgoSchedule;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgorithmStatus;
import org.conferatus.timetable.backend.converter.DtoConverter;
import org.conferatus.timetable.backend.dto.*;
import org.conferatus.timetable.backend.dto.TableNasrano.Nasrano;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.model.enums.TableTime;
import org.conferatus.timetable.backend.services.ScheduleAlgorithmService;
import org.conferatus.timetable.backend.services.ScheduleService;
import org.conferatus.timetable.backend.services.TimeTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Predicate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/timetable")
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final ScheduleService scheduleService;
    private final ScheduleAlgorithmService algoService;
    private final DtoConverter dtoConverter;

    private final Map<Long, Nasrano> currentScheduleByUniversity = new HashMap<>();


    private static void throwServerExceptionIfBothParametersAreNull(String name, Long id) {
        if (name == null && id == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "Id or name parameters should be presented");
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<Long> generateTimetable(@RequestParam Long semesterId,
                                                  @AuthenticationPrincipal User user) {
        var task = scheduleService.generate(semesterId, user);
        return ResponseEntity.ok(task.id());
    }

    @GetMapping("generate/choose")
    public ResponseEntity<Nasrano> chooseGeneratedTimetable(@RequestParam(required = false) Long taskId,
                                                            @RequestParam(defaultValue = "0") int chooseIndex,
                                                            @AuthenticationPrincipal User user) {
        AlgorithmStatus algorithmStatus = taskId != null
                ? algoService.getTaskStatus(taskId)
                : algoService.getLastResult(user.getUniversity());

        List<AlgoSchedule> aboba = algorithmStatus.getResult().join();
        List<Nasrano> nasranos = new ArrayList<>();
        for (int i = 0; i < aboba.size(); i++) {
            var algoSchedule = aboba.get(i);
            nasranos.add(dtoConverter.convert(algoSchedule, i));
        }
        currentScheduleByUniversity.put(user.getUniversity().id(), nasranos.get(chooseIndex));

        return ResponseEntity.ok(nasranos.get(chooseIndex));
    }

    @GetMapping("generate/state")
    public ResponseEntity<AlgorithmStatus.StatusDto> getTaskState(@RequestParam(required = false) Long taskId,
                                                                  @AuthenticationPrincipal User user) {
        AlgorithmStatus algorithmStatus = taskId != null
                ? algoService.getTaskStatus(taskId)
                : algoService.getLastResult(user.getUniversity());
        return ResponseEntity.ok(algorithmStatus == null ? null : algorithmStatus.toStatusDto());
    }

    @GetMapping("generate/result")
    public ResponseEntity<List<Nasrano>> getTaskResult(@RequestParam(required = false) Long taskId,
                                                       @AuthenticationPrincipal User user) {
        AlgorithmStatus algorithmStatus = taskId != null
                ? algoService.getTaskStatus(taskId)
                : algoService.getLastResult(user.getUniversity());
        List<AlgoSchedule> aboba = algorithmStatus.getResult().join();
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
            @RequestParam(value = "id", required = false) Long id,
            @AuthenticationPrincipal User user
    ) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                228L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        Predicate<StudyGroupResponseDTO> predicate = name == null
                ? (group) -> Objects.equals(group.id(), id)
                : (group) -> group.name().equals(name);

        Nasrano nasrano = currentScheduleByUniversity.get(user.getUniversity().id());
        if (nasrano != null) {
            for (LessonDTO lessonDTO : nasrano.timeListDTO().cells()) {
                for (StudyGroupResponseDTO group : lessonDTO.groups()) {
                    if (predicate.test(group)) {
                        res.cells().add(lessonDTO);
                        break;
                    }
                }
            }
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/lessons/by_teacher")
    public ResponseEntity<TimeListDTO> getTeacherLessons(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "id", required = false) Long id,
                                                         @AuthenticationPrincipal User user) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                229L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        Predicate<SimpleTeacher> predicate = name == null
                ? (teacher) -> Objects.equals(teacher.id(), id)
                : (teacher) -> teacher.name().equals(name);
        for (LessonDTO lessonDTO : currentScheduleByUniversity.get(user.getUniversity().id()).timeListDTO().cells()) {
            if (predicate.test(lessonDTO.teacher())) {
                res.cells().add(lessonDTO);
            }
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/lessons/by_audience")
    public ResponseEntity<TimeListDTO> getAudienceLessons(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "id", required = false) Long id,
                                                          @AuthenticationPrincipal User user) {
        throwServerExceptionIfBothParametersAreNull(name, id);

        TimeListDTO res = new TimeListDTO(
                230L,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                new ArrayList<>()
        );
        Predicate<AudienceDTO> predicate = name == null
                ? (audience) -> Objects.equals(audience.id(), id)
                : (audience) -> audience.name().equals(name);
        for (LessonDTO lessonDTO : currentScheduleByUniversity.get(user.getUniversity().id()).timeListDTO().cells()) {
            if (predicate.test(lessonDTO.auditory())) {
                res.cells().add(lessonDTO);
            }
        }
        return ResponseEntity.ok(res);
    }
}
