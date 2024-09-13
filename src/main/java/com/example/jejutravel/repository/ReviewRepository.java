package com.example.jejutravel.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jejutravel.domain.Entity.Review;
import com.example.jejutravel.domain.Entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("select r from Review r where r.contentId=:contentId and r.reviewDeleteYn=false")
	Page<Review> findByContentId(Long contentId, Pageable pageable);

	@Query("select r from Review r where r.contentId=:contentId and r.reviewDeleteYn=false and r.sentiment='positive'")
	Page<Review> findByContentIdAndSentimentIsPositive(Long contentId, Pageable pageable);

	@Query("select r from Review r where r.contentId=:contentId and r.reviewDeleteYn=false and r.sentiment='negative'")
	Page<Review> findByContentIdAndSentimentIsNegative(Long contentId, Pageable pageable);

	@Query("select r from Review r where r.user=:user and r.reviewDeleteYn=false")
	List<Review> findByUser(User user);

	@Query("select r.reviewRating from Review r where r.contentId=:contentId and r.reviewDeleteYn=false")
	List<Integer> findReviewRatingByContentId(Long contentId);
}
