package org.conferatus.timetable.backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
@ToString(exclude = {"studyGroups", "teachers", "audiences", "schedules"})
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<StudyGroup> studyGroups = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Audience> audiences = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<SemesterPlan> semesterPlans = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Schedule> schedules = new ArrayList<>();
}
