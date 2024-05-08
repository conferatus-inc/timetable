package org.conferatus.timetable.backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany
    private List<StudyGroup> studyGroups = new ArrayList<>();

    @OneToMany
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany
    private List<Audience> audiences = new ArrayList<>();

    @OneToMany
    private List<Schedule> schedules = new ArrayList<>();
}
