package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.conferatus.timetable.backend.model.AudienceType;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private AudienceType audienceType;
    private String name;
    @OneToMany
    @JoinColumn(name = "lesson_id")
    private List<Lesson> lessons;


}
