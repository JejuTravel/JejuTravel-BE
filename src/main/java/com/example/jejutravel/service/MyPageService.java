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

    // 개인정보 조회
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
    // 개인정보 수정
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

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        System.out.println("user: " + user.getUserEmail());
        // 기존의 모든 필수 필드들을 유지한 채로 비밀번호만 업데이트
        user = user.toBuilder()
                .userPassword(passwordEncoder.encode(newPassword))
                .userEmail(user.getUserEmail()) // 필수 필드 추가
                .userName(user.getUserName()) // 필수 필드 추가
                .userDateOfBirth(user.getUserDateOfBirth()) // 필수 필드 추가
                .userPhoneNumber(user.getUserPhoneNumber()) // 필수 필드 추가
                .userGender(user.isUserGender()) // 필수 필드 추가
                .userCreatedAt(user.getUserCreatedAt()) // 생성일시 유지
                .userUpdatedAt(new Date(System.currentTimeMillis())) // 수정일시
                .build();

        userRepository.save(user);

    }


}
