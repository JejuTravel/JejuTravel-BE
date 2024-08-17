package com.example.jejutravel.service;

import com.example.jejutravel.domain.Dto.MyPageResponse;
import com.example.jejutravel.domain.Dto.MyPageUpdateRequest;
import com.example.jejutravel.domain.Entity.User;
import com.example.jejutravel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MyPageResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return MyPageResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userDateOfBirth(user.getUserDateOfBirth())
                .userPhoneNumber(user.getUserPhoneNumber())
                .userGender(user.isUserGender())
                .userEmail(user.getUserEmail())
                .build();
    }

    @Transactional
    public void updateUser(Long userId, MyPageUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().isEmpty()) {
            if (!passwordEncoder.matches(updateRequest.getCurrentPassword(), user.getUserPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            user = user.toBuilder()
                    .userPassword(passwordEncoder.encode(updateRequest.getNewPassword()))
                    .build();
        }

        user = user.toBuilder()
                .userName(updateRequest.getUserName())
                .userPhoneNumber(updateRequest.getUserPhoneNumber())
                .userGender(updateRequest.isUserGender())
                .userEmail(updateRequest.getUserEmail())
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();

        userRepository.save(user);
    }
}
