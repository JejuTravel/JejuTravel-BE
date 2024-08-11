package com.example.jejutravel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jejutravel.domain.Dto.review.ReviewListResponse;
import com.example.jejutravel.domain.Dto.review.ReviewResponse;
import com.example.jejutravel.domain.Dto.review.ReviewSaveRequset;
import com.example.jejutravel.domain.Dto.review.ReviewUpdateRequest;
import com.example.jejutravel.domain.Entity.Review;
import com.example.jejutravel.domain.Entity.User;
import com.example.jejutravel.repository.ReviewRepository;
import com.example.jejutravel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	@Transactional
	public ReviewResponse save(ReviewSaveRequset reviewSaveRequset) {

		Long userId = reviewSaveRequset.getUserId();

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 user가 없습니다."));


		Review review = reviewSaveRequset.toEntity(user);
		reviewRepository.save(review);

		ReviewResponse response = new ReviewResponse(review);

		return response;
	}

	@Transactional
	public ReviewResponse updateReview(ReviewUpdateRequest reviewUpdateRequest) {

		Long reviewId = reviewUpdateRequest.getReviewId();

		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException("해당 review가 없습니다."));

		review.updateReview(reviewUpdateRequest);
		reviewRepository.save(review);

		ReviewResponse response = new ReviewResponse(review);

		return response;
	}

	@Transactional
	public void deleteReview(Long reviewId) {

		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException("해당 review가 없습니다."));

		review.updateReviewDeleteYn();
		reviewRepository.save(review);
	}

	@Transactional
	public List<ReviewListResponse> findByContentId(Long contentId) {
		return reviewRepository.findByContentId(contentId)
			.stream().map(ReviewListResponse::new)
			.toList();
	}

	@Transactional
	public Double getAverageRating(Long contentId) {

		List<Integer> ratings = reviewRepository.findReviewRatingByContentId(contentId);

		Integer sum = 0;
		for(Integer rating : ratings){
			sum += rating ;
		}
		return (double)(sum/ratings.size());
	}

	@Transactional
	public List<ReviewListResponse> findByUserId(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 user가 없습니다."));

		return reviewRepository.findByUser(user)
			.stream().map(ReviewListResponse::new)
			.toList();
	}
}
