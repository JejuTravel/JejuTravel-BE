package com.example.jejutravel.repository;

import com.example.jejutravel.domain.Entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findByKakaoId(String kakaoId);
}
