package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.model.AuditoryType;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Auditory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private AuditoryType auditoryType;
    private String name;
    @OneToMany
    @JoinColumn(name = "lesson_id")
    private List<Lesson> auditory;


}
