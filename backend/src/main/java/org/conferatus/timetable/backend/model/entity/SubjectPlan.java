package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.conferatus.timetable.backend.model.enums.AudienceType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subject_plan")
@Accessors(fluent = true)
public class SubjectPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private AudienceType subjectType;
    private Long timesPerWeek;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(fetch = FetchType.EAGER)
    private List<StudyGroup> groups = new ArrayList<>();
}
