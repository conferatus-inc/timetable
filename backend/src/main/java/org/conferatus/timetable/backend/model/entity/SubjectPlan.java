package org.conferatus.timetable.backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.conferatus.timetable.backend.model.SubjectType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subject_plan")
@Accessors(fluent = true)
public class SubjectPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long times;
    @OneToMany
    @JoinColumn(name = "subject_teacher_id")
    List<SubjectTeacher> teachers = new ArrayList<>();
    private String name;
    private SubjectType subjectType;
}
