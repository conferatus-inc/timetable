package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.model.AuditoryType;

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


}
