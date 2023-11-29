package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.model.entity.Subject;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.repos.SubjectRepository;
import org.conferatus.timetable.backend.model.repos.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherToSubjectService {
    private final TeacherService teacherService;
    private final SubjectService subjectService;

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;


    public void addTeacherSubjectLink(Long teacherId, Long subjectId) {
        Teacher teacher = teacherService.getTeacherByIdOrThrow(teacherId);
        Subject subject = subjectService.getSubjectByIdOrThrow(subjectId);

        teacher.getPossibleSubjects().add(subject);
        subject.getPossibleTeachers().add(teacher);

        teacherRepository.save(teacher);
        subjectRepository.save(subject);
    }
}
