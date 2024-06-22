package org.conferatus.timetable.backend.model.entity;

import java.time.DayOfWeek;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.dto.TeacherWishDto;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherWish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Teacher teacher;

    private DayOfWeek dayOfWeek;

    private long lessonNumber;

    private long priority;

    public TeacherWishDto toWishDto() {
        return new TeacherWishDto(id, dayOfWeek, lessonNumber, priority);
    }
}
