package org.conferatus.timetable.backend.model.entity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    @ManyToMany
    List<StudyGroup> groups = new ArrayList<>();
}
