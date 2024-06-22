package org.conferatus.timetable.backend.repository;

import org.conferatus.timetable.backend.model.entity.TeacherWish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherWishRepository extends JpaRepository<TeacherWish, Long> {
}