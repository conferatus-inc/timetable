package org.conferatus.timetable.backend.repository;

import java.time.DayOfWeek;
import java.util.Optional;

import org.conferatus.timetable.backend.model.entity.TeacherWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface TeacherWishRepository extends JpaRepository<TeacherWish, Long> {
    @Query("select t from TeacherWish t where t.teacher.id = ?1 and t.dayOfWeek = ?2 and t.lessonNumber = ?3")
    Optional<TeacherWish> findByTeacher_IdAndDayOfWeekAndLessonNumber(@NonNull Long id, @NonNull DayOfWeek dayOfWeek,
                                                                      @NonNull long lessonNumber);
}