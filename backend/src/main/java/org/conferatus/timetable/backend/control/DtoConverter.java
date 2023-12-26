package org.conferatus.timetable.backend.control;

import lombok.AllArgsConstructor;
import org.conferatus.timetable.backend.algorithm.scheduling.GeneticAlgorithmScheduler.AlgoSchedule;
import org.conferatus.timetable.backend.algorithm.scheduling.LessonWithTime;
import org.conferatus.timetable.backend.control.dto.*;
import org.conferatus.timetable.backend.control.dto.TableNasrano.Nasrano;
import org.conferatus.timetable.backend.model.TableTime;
import org.conferatus.timetable.backend.model.repos.StudyGroupRepository;
import org.conferatus.timetable.backend.model.repos.SubjectPlanRepository;
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
    private final StudyGroupRepository groupRepository;
    private final SubjectPlanRepository subjectPlanRepository;

    public Nasrano convert(AlgoSchedule algoSchedule, long id) {
        TimeListDTO timeListDTO;

        List<LessonDTO> cells = new ArrayList<>();
        for (LessonWithTime lessonWithTime : algoSchedule.allLessons()) {
            var subject = lessonWithTime.lessonGene().subject();
            var teacher = lessonWithTime.teacher();
            List<StudyGroupResponseDTO> studyGroupResponseDTOS = lessonWithTime.groups().stream().map(
                    groupEvolve -> new StudyGroupResponseDTO(groupEvolve.id(),
                            groupRepository.findStudyGroupById(groupEvolve.id()).get().getName())
            ).toList();


            LessonDTO lessonDTO = new LessonDTO(
                    subject.id(),
                    subjectPlanRepository.getById(subject.id()).name(),
                    new SimpleTeacher(teacher.id(), teacherRepository.getById(teacher.id()).teacher().getName()),
                    new AudienceDTO(lessonWithTime.audience().id(),
                            audienceService.getAudience(lessonWithTime.audience().id()).getName(),
                            lessonWithTime.audience().auditoryType()),
                    studyGroupResponseDTOS
                    ,
                    lessonWithTime.time().day(),
                    lessonWithTime.time().cellNumber()
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
