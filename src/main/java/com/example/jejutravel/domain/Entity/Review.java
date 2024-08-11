package com.example.jejutravel.domain.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.jejutravel.domain.Dto.review.ReviewUpdateRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "content_id", nullable = false)
	private Long contentId;

	@Column(name = "review_content")
	private String reviewContent;

	@Column(name = "review_rating", nullable = false)
	private Integer reviewRating;

	@Column(name = "review_created_at", nullable = false)
	private Date reviewCreatedAt;

	@Column(name = "review_updated_at", nullable = false)
	private Date reviewUpdatedAt;

	@Column(name = "review_delete_yn", nullable = false, columnDefinition = "boolean default false")
	private boolean reviewDeleteYn;


	@Builder
	public Review(User user, Long contentId, String reviewContent, Integer reviewRating, Date reviewCreatedAt, Date reviewUpdatedAt, boolean reviewDeleteYn) {
		this.user = user;
		this.contentId = contentId;
		this.reviewContent = reviewContent;
		this.reviewRating = reviewRating;
		this.reviewCreatedAt = reviewCreatedAt;
		this.reviewUpdatedAt = reviewUpdatedAt;
		this.reviewDeleteYn = reviewDeleteYn;
	}

	public void updateReview(ReviewUpdateRequest request) {
		this.reviewContent = request.getReviewContent();
		this.reviewRating = request.getReviewRating();
		this.reviewUpdatedAt = new Date(System.currentTimeMillis());
	}

	public void updateReviewDeleteYn() {
		this.reviewDeleteYn = true;
	}
}
