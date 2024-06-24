package org.conferatus.timetable.backend.repository;

import org.conferatus.timetable.backend.model.entity.Role;
import org.conferatus.timetable.backend.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
