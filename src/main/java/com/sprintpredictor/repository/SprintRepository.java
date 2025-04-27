package com.sprintpredictor.repository;

import com.sprintpredictor.entity.Sprint;
import com.sprintpredictor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByUser(User user);
    List<Sprint> findByUserAndStartDateBetween(User user, LocalDate start, LocalDate end);

    Optional<Sprint> findByIdAndUser(Long id, User user); // ➔ Add this!
}
