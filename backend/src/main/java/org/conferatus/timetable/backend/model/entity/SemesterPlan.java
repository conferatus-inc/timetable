package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "semesterplan")
@Builder
public class SemesterPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subject_id")
    private List<Subject> subjects = new ArrayList<>();
    //todo: ? manytomany
    //@JoinTable
}
