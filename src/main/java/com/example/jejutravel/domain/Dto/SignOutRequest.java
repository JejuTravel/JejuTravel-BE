package com.example.jejutravel.domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignOutRequest {

    private String userUsername;

    @Builder    
    public SignOutRequest(String userUsername) {
        this.userUsername = userUsername;
    }
    
}
