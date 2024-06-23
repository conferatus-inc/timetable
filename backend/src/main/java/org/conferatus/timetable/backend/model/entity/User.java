package org.conferatus.timetable.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.dto.UserDto;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String accessToken;
    private String refreshToken;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    private University university;

    public boolean checkUniversityAccess(long id) {
        return university.id() == id;
    }

    public UserDto toUserDto() {
        return new UserDto(
                id,
                username,
                university.toUniversityDto()
        );
    }
}
