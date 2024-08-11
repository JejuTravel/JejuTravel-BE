package com.example.jejutravel.domain.Dto.review;

import java.util.Date;

import com.example.jejutravel.domain.Entity.Review;
import com.example.jejutravel.domain.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewSaveRequset {

	private Long userId;
	private Long contentId;
	private String reviewContent;
	private Integer reviewRating;

	public ReviewSaveRequset(Review review) {
		this.userId = review.getUser().getUserId();
		this.contentId = review.getContentId();
		this.reviewContent = review.getReviewContent();
		this.reviewRating = review.getReviewRating();
	}

	public Review toEntity(User user) {
		return Review.builder()
			.user(user)
			.contentId(contentId)
			.reviewContent(reviewContent)
			.reviewRating(reviewRating)
			.reviewCreatedAt(new Date(System.currentTimeMillis()))
			.reviewUpdatedAt(new Date(System.currentTimeMillis()))
			.build();
	}
}
