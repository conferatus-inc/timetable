package org.conferatus.timetable.backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.model.AudienceType;

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

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "lesson_id")
    private List<Lesson> lessons = new ArrayList<>();


}
