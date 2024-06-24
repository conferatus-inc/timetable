package org.conferatus.timetable.backend.services;

import lombok.RequiredArgsConstructor;
import org.conferatus.timetable.backend.dto.TeacherWishDto;
import org.conferatus.timetable.backend.exception.ServerException;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.entity.TeacherWish;
import org.conferatus.timetable.backend.model.entity.University;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.repository.TeacherRepository;
import org.conferatus.timetable.backend.repository.TeacherWishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherWishRepository teacherWishRepository;

    public Teacher getTeacherByUserAndIdOrThrow(User user, Long id) {
        var teacher = teacherRepository.findTeacherById(id);
        if (teacher.isEmpty() ||
                teacher.get().getUniversity() == null ||
                !user.checkUniversityAccess(teacher.get().getUniversity().id())) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Teacher with id %s in university %s does not exist",
                            id, user.getUniversity().id()));
        }
        return teacher.get();
    }

    private Teacher getTeacherByUserAndNameOrThrow(User user, String name) {
        var teacher = teacherRepository.findByNameAndUniversity_Id(name, user.getUniversity().id());
        if (teacher.isEmpty()) {
            throw new ServerException(HttpStatus.NOT_FOUND,
                    String.format("Teacher with name %s in university %s does not exist",
                            name, user.getUniversity().id()));
        }
        return teacher.get();
    }

    private void notExistsByNameOrThrow(User user, String name) {
        var teacher = teacherRepository.findByNameAndUniversity_Id(name, user.getUniversity().id());
        if (teacher.isPresent()) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    String.format("Teacher with name %s in university %s already exists",
                            name, user.getUniversity().id()));
        }
    }

    private void notExistsByNameOrThrow(String name) {
        var teacher = teacherRepository.findTeacherByName(name);
        if (teacher.isPresent()) {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    String.format("Teacher with name %s already exists", name));
        }
    }

    public Teacher getTeacher(User user, Long id) {
        return getTeacherByUserAndIdOrThrow(user, id);
    }

    public Teacher getTeacher(User user, String name) {
        return getTeacherByUserAndNameOrThrow(user, name);
    }

    public List<Teacher> getAllTeachers(University university) {
        return teacherRepository.findAll().stream()
                .filter(it -> it.getUniversity() != null && it.getUniversity().id() == university.id()).toList();
    }

    public Teacher addTeacher(User user, String teacherName) {
        notExistsByNameOrThrow(teacherName);
        var teacher = new Teacher();
        teacher.setName(teacherName);
        teacher.setUniversity(user.getUniversity());
        return teacherRepository.save(teacher);
    }


    public Teacher updateTeacher(User user, String previousTeacherName, String newTeacherName) {
        var teacher = getTeacherByUserAndNameOrThrow(user, previousTeacherName);
        if (newTeacherName.equals(previousTeacherName)) {
            return teacher;
        }
        notExistsByNameOrThrow(user, newTeacherName);
        teacher.setName(newTeacherName);
        return teacherRepository.save(teacher);
    }


    public Teacher deleteTeacherOrThrow(User user, String teacherName) {
        var teacher = getTeacherByUserAndNameOrThrow(user, teacherName);
        teacherRepository.delete(teacher);
        return teacher;
    }

    public Teacher addTeacherWish(User user, String teacherName, TeacherWishDto teacherWish) {
        var teacher = getTeacherByUserAndNameOrThrow(user, teacherName);
        teacherWishRepository.findByTeacher_IdAndDayOfWeekAndLessonNumber(
                teacher.getId(),
                teacherWish.dayOfWeek(),
                teacherWish.lessonNumber()
        ).ifPresent(foundTeacherWith -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    String.format("TeacherWish for teacher %s for day %s and lesson %s already exists",
                            teacherName, teacherWish.dayOfWeek(), teacherWish.lessonNumber()));
        });
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
