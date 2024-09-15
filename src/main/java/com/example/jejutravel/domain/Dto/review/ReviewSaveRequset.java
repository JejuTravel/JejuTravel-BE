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
	private Long contentTypeId;
	private String cat3;
	private String reviewContent;
	private Integer reviewRating;

	public ReviewSaveRequset(Review review) {
		this.userId = review.getUser().getUserId();
		this.contentId = review.getContentId();
		this.contentTypeId = review.getContentTypeId();
		this.cat3 = review.getCat3();
		this.reviewContent = review.getReviewContent();
		this.reviewRating = review.getReviewRating();
	}

	public Review toEntity(User user) {
		return Review.builder()
			.user(user)
			.contentId(contentId)
			.contentTypeIdId(contentTypeId)
			.cat3(cat3)
			.reviewContent(reviewContent)
			.reviewRating(reviewRating)
			.reviewCreatedAt(new Date(System.currentTimeMillis()))
			.reviewUpdatedAt(new Date(System.currentTimeMillis()))
			.build();
	}
}
