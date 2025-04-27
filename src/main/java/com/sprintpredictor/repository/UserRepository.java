// src/main/java/com/sprintpredictor/repository/UserRepository.java
package com.sprintpredictor.repository;

import com.sprintpredictor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}