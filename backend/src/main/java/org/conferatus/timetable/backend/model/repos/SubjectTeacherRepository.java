package org.conferatus.timetable.backend.model.repos;

import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.SubjectTeacher;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTeacherRepository extends JpaRepository<SubjectTeacher, Long> {
    boolean existsSubjectPlanByTeacherName(String teacherName);

    void deleteSubjectTeacherByTeacherName(String teacherName);

    Optional<SubjectTeacher> findSubjectTeacherByTeacherName(String previousTeacherName);

    Optional<SubjectTeacher> findSubjectTeachersByTeacher(Teacher teacher);
}
