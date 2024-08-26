package com.example.jejutravel.domain.Dto.review;

import java.util.Date;

import com.example.jejutravel.domain.Entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponse {

	private Long reviewId;
	private Long userId;
	private Long contentId;
	private String reviewContent;
	private Integer reviewRating;
	private Date reviewCreatedAt;
	private Date reviewUpdatedAt;
	private String sentiment;

	public ReviewResponse(Review review) {
		this.reviewId = review.getReviewId();
		this.userId = review.getUser().getUserId();
		this.contentId = review.getContentId();
		this.reviewContent = review.getReviewContent();
		this.reviewRating = review.getReviewRating();
		this.reviewCreatedAt = review.getReviewCreatedAt();
		this.reviewUpdatedAt = review.getReviewUpdatedAt();
		this.sentiment = review.getSentiment();
	}
}
