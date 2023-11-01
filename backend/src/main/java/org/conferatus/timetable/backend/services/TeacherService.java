package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.repos.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    private Teacher getTeacherByIdOrThrow(Long id) {
        return teacherRepository.findTeacherById(id)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Teacher with id " + id + " does not exist"));
    }

    private Teacher getTeacherByNameOrThrow(String name) {
        return teacherRepository.findTeacherByName(name)
                .orElseThrow(() -> new ServerException(HttpStatus.NOT_FOUND,
                        "Teacher with name " + name + " does not exist"));
    }

    private void notExistsByNameOrThrow(String name) {
        teacherRepository.findTeacherByName(name).ifPresent(teacher -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Teacher with name " + name + " already exists");
        });
    }


    public Teacher getTeacher(Long id) {
        return getTeacherByIdOrThrow(id);
    }

    public Teacher addTeacher(String teacherName) {
        notExistsByNameOrThrow(teacherName);
        return teacherRepository.save(Teacher.builder().name(teacherName).build());
    }


    public Teacher updateTeacher(String previousTeacherName, String newTeacherName) {
        var teacher = getTeacherByNameOrThrow(previousTeacherName);
        if (newTeacherName.equals(previousTeacherName)) {
            return teacher;
        }
        notExistsByNameOrThrow(newTeacherName);
        teacher.setName(newTeacherName);
        return teacherRepository.save(teacher);
    }


    public Teacher deleteTeacherOrThrow(String teacherName) {
        var teacher = getTeacherByNameOrThrow(teacherName);
        teacherRepository.delete(teacher);
        return teacher;
    }
}
