package com.example.jejutravel.domain.Dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
public class MyPageResponse {
    private Long userId;
    private String userName;
    private Date userDateOfBirth;
    private String userPhoneNumber;
    private boolean userGender;
    private String userEmail;

    @Builder
    public MyPageResponse(Long userId, String userName, Date userDateOfBirth, String userPhoneNumber,
            boolean userGender, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
        this.userPhoneNumber = userPhoneNumber;
        this.userGender = userGender;
        this.userEmail = userEmail;
    }
}