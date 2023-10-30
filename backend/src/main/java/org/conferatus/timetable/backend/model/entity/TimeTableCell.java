package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeTableCell {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToMany
    @JoinTable //fixme
    private List<Group> groups;
    @ManyToOne
    @JoinColumn
    private Teacher teacher;
    @ManyToOne
    @JoinColumn
    private Subject subject;
    @ManyToOne
    @JoinColumn
    private Auditory auditory;
}
