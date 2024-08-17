package com.example.jejutravel.repository;

import com.example.jejutravel.domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserUsername(String userUsername);

    boolean existsByUserUsername(String userUsername);

    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserId(Long userId);
}
