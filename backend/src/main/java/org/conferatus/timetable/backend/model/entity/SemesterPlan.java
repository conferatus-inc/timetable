package org.conferatus.timetable.backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "semesterplan")
@Accessors(fluent = true)
public class SemesterPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subject_plan_id")
    private List<SubjectPlan> subjectPlans = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "study_group_id")
    private List<StudyGroup> studyGroups = new ArrayList<>();
}
