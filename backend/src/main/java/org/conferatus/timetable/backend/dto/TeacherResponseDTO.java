package org.conferatus.timetable.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.conferatus.timetable.backend.model.entity.Teacher;
import org.conferatus.timetable.backend.model.entity.TeacherWish;

public record TeacherResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
//        List<SubjectPlanDTO> possibleSubjects,
        List<LessonDTO> lessons,
        List<TeacherWishDto> teacherWishDtos
) {
    public TeacherResponseDTO(Teacher teacher) {
        this(
                teacher.getId(),
                teacher.getName(),
//                teacher.getPossibleSubjects().stream().map(SubjectPlanDTO::new).collect(Collectors.toList()),
                teacher.getLessons().stream().map(LessonDTO::new).toList(),
                teacher.getTeacherWishes().stream().map(TeacherWish::toWishDto).toList()
        );
    }
}
