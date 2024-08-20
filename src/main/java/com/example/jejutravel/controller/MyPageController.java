package com.example.jejutravel.controller;

import com.example.jejutravel.domain.Dto.MyPageResponse;
import com.example.jejutravel.domain.Dto.MyPageUpdateRequest;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.service.MyPageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    // 개인정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyPage() {
        logger.info("Entered MyPageController.getMyPage");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("No authentication information found.");
                return ResponseEntity.status(401).body(ApiResponse.createError("Unauthorized"));
            }

            User userDetails = (User) authentication.getPrincipal();
            logger.info("Authenticated user: {}", userDetails.getUsername());

            Long userId = Long.parseLong(userDetails.getUsername());

            MyPageResponse responseDTO = myPageService.getUser(userId);
            return ResponseEntity.ok(ApiResponse.createSuccess(responseDTO));
        } catch (Exception e) {
            logger.warn("Unauthorized access attempt.", e);
            return ResponseEntity.status(401).body(ApiResponse.createError("Unauthorized"));
        }
    }
    // 개인정보 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateMyPage(@RequestBody MyPageUpdateRequest myPageUpdate) {
        logger.info("Entered MyPageController.updateMyPage");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();
            logger.info("Authenticated user: {}", userDetails.getUsername());

            if (userDetails.getUsername().equals("anonymous")) {
                logger.warn("Unauthorized update attempt.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("Unauthorized"));
            }

            Long userId = Long.parseLong(userDetails.getUsername());
            logger.info("User ID parsed from token: {}", userId);

            myPageService.updateUser(userId, myPageUpdate);
            // 수정 후, 갱신된 사용자 정보를 다시 조회
            MyPageResponse updatedResponse = myPageService.getUser(userId);

            return ResponseEntity
                    .ok(ApiResponse.createSuccessWithMessage(updatedResponse, "User information updated successfully"));
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.createError("Invalid user ID format."));
        } catch (Exception e) {
            logger.error("Exception in MyPageController.updateMyPage: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(e.getMessage()));
        }
    }
    // 비밀번호 변경
    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@RequestBody MyPageUpdateRequest myPageUpdate) {
        logger.info("Entered MyPageController.updatePassword");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();
            logger.info("Authenticated user: {}", userDetails.getUsername());

            if (userDetails.getUsername().equals("anonymous")) {
                logger.warn("Unauthorized password update attempt.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("Unauthorized"));
            }

            Long userId = Long.parseLong(userDetails.getUsername());
            logger.info("User ID parsed from token: {}", userId);

            myPageService.updateUser(userId, myPageUpdate);
            return ResponseEntity
                    .ok(ApiResponse.createSuccessWithMessage(null, "Password updated successfully"));
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.createError("Invalid user ID format."));
        } catch (Exception e) {
            logger.error("Exception in MyPageController.updatePassword: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(e.getMessage()));
        }
    }
}