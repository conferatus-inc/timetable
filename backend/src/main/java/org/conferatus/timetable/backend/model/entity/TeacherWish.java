package org.conferatus.timetable.backend.model.entity;

import java.time.DayOfWeek;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeacherWish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Teacher teacher;

    private DayOfWeek dayOfWeek;

    @Column(columnDefinition = "INT(11) CHECK lesson_number >= 1 AND your_integer_column <= 7")
    private long lessonNumber;

    @Column(columnDefinition = "INT(11) CHECK priority >= -10 AND your_integer_column <= 10")
    private long priority;
}
