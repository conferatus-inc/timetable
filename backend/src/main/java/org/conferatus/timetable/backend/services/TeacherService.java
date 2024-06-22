package org.conferatus.timetable.backend.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.TeacherWishDto;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.entity.TeacherWish;
import org.conferatus.timetable.backend.repository.TeacherRepository;
import org.conferatus.timetable.backend.repository.TeacherWishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherWishRepository teacherWishRepository;

    public Teacher getTeacherByIdOrThrow(Long id) {
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

    public Teacher getTeacher(String name) {
        return getTeacherByNameOrThrow(name);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher addTeacher(String teacherName) {
        notExistsByNameOrThrow(teacherName);
        var teacher = new Teacher();
        teacher.setName(teacherName);
        return teacherRepository.save(teacher);
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

    public Teacher addTeacherWish(String teacherName, TeacherWishDto teacherWish) {
        var teacher = getTeacherByNameOrThrow(teacherName);
        var newTeacherWish = new TeacherWish();
        newTeacherWish.setTeacher(teacher);
        newTeacherWish.setPriority(teacherWish.priority());
        newTeacherWish.setDayOfWeek(teacherWish.dayOfWeek());
        newTeacherWish.setLessonNumber(teacherWish.lessonNumber());
        newTeacherWish = teacherWishRepository.save(newTeacherWish);
        teacher.getTeacherWishes().add(newTeacherWish);
        return teacherRepository.save(teacher);
    }
}
