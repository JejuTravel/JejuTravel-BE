package com.example.jejutravel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jejutravel.domain.Dto.GlobalResponse;
import com.example.jejutravel.domain.Dto.SignInRequest;
import com.example.jejutravel.domain.Dto.SignInResponse;
import com.example.jejutravel.domain.Dto.SignUpRequest;
import com.example.jejutravel.domain.Dto.SignUpResponse;
import com.example.jejutravel.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);
        if (response.getMsg().equals("회원가입 성공")) {
            return ResponseEntity.ok().body(GlobalResponse.builder()
                    .message(HttpStatus.OK.getReasonPhrase())
                    .status(HttpStatus.OK.value())
                    .data(response)
                    .build());
        }
        return ResponseEntity.badRequest().body(GlobalResponse.builder()
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .data(response)
                .build());
    }

    @PostMapping("/signin")
    public ResponseEntity<GlobalResponse> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = userService.signIn(request);
        if (response.getMsg().equals("로그인 성공")) {
            return ResponseEntity.ok().body(GlobalResponse.builder()
                    .message(HttpStatus.OK.getReasonPhrase())
                    .status(HttpStatus.OK.value())
                    .data(response)
                    .build());
        }
        return ResponseEntity.badRequest().body(GlobalResponse.builder()
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .data(response)
                .build());
    }

    @PostMapping("/signout")
    public ResponseEntity<GlobalResponse> signOut(@RequestBody Map<String, String> request)
            throws JsonProcessingException {
        String token = request.get("token");
        String response = userService.signOut(token);
        if (response.equals("로그아웃 실패")) {
            return ResponseEntity.badRequest().body(GlobalResponse.builder()
                    .message(response)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build());
        }
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .message(response)
                .status(HttpStatus.OK.value())
                .build());
    }
}
