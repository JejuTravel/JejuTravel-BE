package com.example.jejutravel.domain.Dto.review;

import com.example.jejutravel.domain.Entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {

	private Long reviewId;
	private String reviewContent;
	private Integer reviewRating;

	public ReviewUpdateRequest(Review review) {
		this.reviewId = review.getReviewId();
		this.reviewContent = review.getReviewContent();
		this.reviewRating = review.getReviewRating();
	}

}
