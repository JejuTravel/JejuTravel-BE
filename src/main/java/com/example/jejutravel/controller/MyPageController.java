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
            return ResponseEntity
                    .ok(ApiResponse.createSuccessWithMessage(null, "User information updated successfully"));
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.createError("Invalid user ID format."));
        } catch (Exception e) {
            logger.error("Exception in MyPageController.updateMyPage: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(e.getMessage()));
        }
    }
}