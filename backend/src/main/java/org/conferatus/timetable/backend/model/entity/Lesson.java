package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private DayOfWeek dayOfWeek;

    @Column(columnDefinition = "INT(11) CHECK lesson_number >= 1 AND your_integer_column <= 7")
    private long lessonNumber;

    @OneToOne
    Audience audience;

    @OneToOne
    Teacher teacher;

    @OneToOne
    StudyGroup group;
}
