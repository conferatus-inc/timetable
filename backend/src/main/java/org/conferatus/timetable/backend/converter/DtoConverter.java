package org.conferatus.timetable.backend.converter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgoSchedule;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.dto.AudienceDTO;
import org.conferatus.timetable.backend.dto.LessonDTO;
import org.conferatus.timetable.backend.dto.SimpleTeacher;
import org.conferatus.timetable.backend.dto.StudyGroupResponseDTO;
import org.conferatus.timetable.backend.dto.TableNasrano.Nasrano;
import org.conferatus.timetable.backend.dto.TimeListDTO;
import org.conferatus.timetable.backend.model.enums.TableTime;
import org.conferatus.timetable.backend.repository.StudyGroupRepository;
import org.conferatus.timetable.backend.repository.SubjectPlanRepository;
import org.conferatus.timetable.backend.repository.TeacherRepository;
import org.conferatus.timetable.backend.services.AudienceService;
import org.conferatus.timetable.backend.services.SemesterPlanService;
import org.conferatus.timetable.backend.services.SubjectPlanService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DtoConverter {
    private final SemesterPlanService semesterPlanService;
    private final AudienceService audienceService;
    private final SubjectPlanService subjectPlanService;
    private final StudyGroupRepository groupRepository;
    private final SubjectPlanRepository subjectPlanRepository;
    private final TeacherRepository teacherRepository;

    public Nasrano convert(AlgoSchedule algoSchedule, long id) {
        TimeListDTO timeListDTO;

        List<LessonDTO> cells = new ArrayList<>();
        // FIXME
        for (LessonWithTime lessonWithTime : algoSchedule.allLessons()) {
            var subject = lessonWithTime.lessonGene().subject();
            var teacher = lessonWithTime.teacher();
            List<StudyGroupResponseDTO> studyGroupResponseDTOS = lessonWithTime.groups().stream().map(
                    groupEvolve -> new StudyGroupResponseDTO(groupEvolve.id(),
                            groupRepository.findStudyGroupById(groupEvolve.id()).get().getName())
            ).toList();


            // FIXME
            LessonDTO lessonDTO = new LessonDTO(
                    subject.id(),
                    subjectPlanRepository.getById(subject.id()).name(),
                    new SimpleTeacher(
                            teacher.id(),
                            teacherRepository.getById(teacher.id()).getName()
                    ),
                    new AudienceDTO(
                            lessonWithTime.audience().id(),
                            audienceService.getAudienceByIdOrThrow(lessonWithTime.audience().id()).getName(),
                            lessonWithTime.audience().groupCapacity()
                    ),
                    studyGroupResponseDTOS,
                    DayOfWeek.of(lessonWithTime.time().day()),
                    (long) lessonWithTime.time().cellNumber()
            );
            cells.add(lessonDTO);
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