package org.conferatus.timetable.backend.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.conferatus.timetable.backend.dto.UserDto;

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
