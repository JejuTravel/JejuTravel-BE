package com.example.jejutravel.domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageUpdateRequest {
    private String userName;
    private String userPhoneNumber;
    private boolean userGender;
    private String userEmail;
    private String currentPassword;
    private String newPassword;

    @Builder
    public MyPageUpdateRequest(String userName, String userPhoneNumber, boolean userGender, String userEmail,
            String currentPassword, String newPassword) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}