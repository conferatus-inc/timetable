package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.model.enums.AudienceType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long audienceGroupCapacity;
    private String name;

    private AudienceType audienceType;

    @ManyToOne
    private University university;
}
