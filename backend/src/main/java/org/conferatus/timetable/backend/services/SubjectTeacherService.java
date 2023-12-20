package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.SubjectTeacher;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.repos.SubjectTeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectTeacherService {
    private final SubjectTeacherRepository subjectTeacherRepository;

    public SubjectTeacher getSubjectTeacherByIdOrThrow(Long id) {
        return subjectTeacherRepository.findSubjectPlanById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SubjectTeacher with id " + id + " does not exist"));
    }

    private SubjectTeacher getSubjectTeacherByNameOrThrow(String name) {
        return subjectTeacherRepository.findSubjectTeacherByTeacherName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "SubjectTeacher with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        if (subjectTeacherRepository.findSubjectTeacherByTeacherName(name).isPresent()) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "SubjectTeacher with name " + name + " already exists");
        }
    }

    public SubjectTeacher addSubjectTeacher(Teacher teacher, Long possibleTimes) {
        SubjectTeacher subjectTeacher = new SubjectTeacher().teacher(teacher).possibleTimes(possibleTimes);
        return subjectTeacherRepository.save(subjectTeacher);
    }
}
