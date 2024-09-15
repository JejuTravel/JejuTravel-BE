package com.example.jejutravel.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jejutravel.domain.Dto.review.ReviewListResponse;
import com.example.jejutravel.domain.Dto.review.ReviewResponse;
import com.example.jejutravel.domain.Dto.review.ReviewSaveRequset;
import com.example.jejutravel.domain.Dto.review.ReviewUpdateRequest;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService ;

	@PostMapping("/review/save")
	public ApiResponse<ReviewResponse> saveReview(@RequestBody ReviewSaveRequset params) {

		return ApiResponse.createSuccessWithMessage(reviewService.save(params), "리뷰 저장 완료");
	}

	@PostMapping("/review/update")
	public ApiResponse<ReviewResponse> updateReview(@RequestBody ReviewUpdateRequest params) {

		return ApiResponse.createSuccessWithMessage(reviewService.updateReview(params),
			"리뷰 수정 완료");
	}

	@PostMapping("/review/delete/{reviewId}")
	public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {

		reviewService.deleteReview(reviewId);

		return ApiResponse.createSuccessWithMessage(null, "리뷰 삭제 완료");
	}

	@GetMapping("/review/{contentId}")
	public ApiResponse<Page<ReviewListResponse>> findReviewByContent(
		@PathVariable Long contentId,
		@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
		@PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

		Page<ReviewListResponse> reviews = reviewService.findByContentId(contentId, pageNumber, pageable);
		return ApiResponse.createSuccess(reviews);
	}

	@GetMapping("/review/positive/{contentId}")
	public ApiResponse<Page<ReviewListResponse>> findPositiveReviewByContent(@PathVariable Long contentId,
		@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
		@PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

		Page<ReviewListResponse> reviews = reviewService.findPositiveReviewByContent(contentId, pageNumber, pageable);
		return ApiResponse.createSuccess(reviews);
	}

	@GetMapping("/review/negative/{contentId}")
	public ApiResponse<Page<ReviewListResponse>> findNegativeReviewByContent(@PathVariable Long contentId,
		@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
		@PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

		Page<ReviewListResponse> reviews = reviewService.findNegativeReviewByContent(contentId, pageNumber, pageable);
		return ApiResponse.createSuccess(reviews);
	}

	@GetMapping("/review/average/{contentId}")
	public ApiResponse<Double> getAverageRating(@PathVariable Long contentId) {

		return ApiResponse.createSuccess(reviewService.getAverageRating(contentId));
	}

	@GetMapping("/review/user/{userId}")
	public ApiResponse<List<ReviewListResponse>> findReviewByUser(@PathVariable Long userId) {

		List<ReviewListResponse> reviews = reviewService.findByUserId(userId);
		return ApiResponse.createSuccess(reviews);
	}

	@GetMapping("review/top2Cat3/{userId}")
	public ResponseEntity<List<String>> getTop2Cat3(@PathVariable Long userId) {
		List<String> top2Cat3 = reviewService.getTop2Cat3ByUserId(userId);
		return ResponseEntity.ok(top2Cat3);
	}
}
