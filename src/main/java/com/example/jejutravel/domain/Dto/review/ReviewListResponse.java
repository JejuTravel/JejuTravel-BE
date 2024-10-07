package com.example.jejutravel.domain.Dto.review;

import java.util.Date;

import com.example.jejutravel.domain.Entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewListResponse {

	private Long reviewId;
	private String userName;
	private Long contentId;
	private String reviewContent;
	private Integer reviewRating;
	private Date reviewUpdatedAt;
	private String sentiment;

	public ReviewListResponse(Review review) {
		this.reviewId = review.getReviewId();
		this.userName = review.getUser().getUserName();
		this.contentId = review.getContentId();
		this.reviewContent = review.getReviewContent();
		this.reviewRating = review.getReviewRating();
		this.reviewUpdatedAt = review.getReviewUpdatedAt();
		this.sentiment = review.getSentiment();
	}

}
