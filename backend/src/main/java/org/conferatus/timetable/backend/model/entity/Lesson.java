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

    private long lessonNumber;

    @ManyToOne
    Audience audience;

    @ManyToOne
    Teacher teacher;

    @ManyToOne
    StudyGroup group;
}
