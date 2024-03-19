package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "studygroup")
@Builder
public class StudyGroup {
    @ManyToMany(mappedBy = "groups", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    List<Lesson> lessons = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

}
