package org.conferatus.timetable.backend.control;

import lombok.AllArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgoSchedule;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.control.dto.AudienceDTO;
import org.conferatus.timetable.backend.control.dto.LessonDTO;
import org.conferatus.timetable.backend.control.dto.SimpleTeacher;
import org.conferatus.timetable.backend.control.dto.TableNasrano.Nasrano;
import org.conferatus.timetable.backend.control.dto.TimeListDTO;
import org.conferatus.timetable.backend.model.TableTime;
import org.conferatus.timetable.backend.model.repos.SubjectTeacherRepository;
import org.conferatus.timetable.backend.services.AudienceService;
import org.conferatus.timetable.backend.services.SemesterPlanService;
import org.conferatus.timetable.backend.services.SubjectPlanService;
import org.conferatus.timetable.backend.services.SubjectTeacherService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DtoConverter {
    private final SemesterPlanService semesterPlanService;
    private final AudienceService audienceService;
    private final SubjectTeacherService subjectTeacherService;
    private final SubjectPlanService subjectPlanService;
    private final SubjectTeacherRepository teacherRepository;

    public Nasrano convert(AlgoSchedule algoSchedule, long id) {
        TimeListDTO timeListDTO;

        List<LessonDTO> cells = new ArrayList<>();
        for (LessonWithTime lessonWithTime : algoSchedule.allLessons()) {
            var subject = lessonWithTime.lessonGene().subject();
            var teacher = lessonWithTime.teacher();
            LessonDTO lessonDTO = new LessonDTO(
                    subject.id(),
                    new SimpleTeacher(teacher.id(), teacherRepository.getById(teacher.id()).teacher().getName()),
                    new AudienceDTO(lessonWithTime.audience().id(),
                            audienceService.getAudience(lessonWithTime.audience().id()).getName(),
                            lessonWithTime.audience().auditoryType()),
                    null,
                    TableTime.getDaysAmount(),
                    TableTime.getCellsAmount()
            );
        }
        timeListDTO = new TimeListDTO(id,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                TableTime.getDaysAmount(),
                TableTime.getCellsAmount(),
                cells
        );
        return new Nasrano(
                timeListDTO,
                algoSchedule.checkResult()
        );
    }
}
