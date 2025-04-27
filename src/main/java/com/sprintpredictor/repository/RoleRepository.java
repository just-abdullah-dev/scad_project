// src/main/java/com/sprintpredictor/repository/RoleRepository.java
package com.sprintpredictor.repository;

import com.sprintpredictor.entity.ERole;
import com.sprintpredictor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}