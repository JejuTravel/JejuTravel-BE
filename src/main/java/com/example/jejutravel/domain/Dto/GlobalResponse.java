package com.example.jejutravel.domain.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalResponse<T> {
    private int status;
    private String message;
    private T data;

    @Builder(toBuilder = true)
    public GlobalResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
