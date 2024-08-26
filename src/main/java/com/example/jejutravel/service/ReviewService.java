package com.example.jejutravel.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.parser.BeanOutputParser;
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

	private final OpenAiChatModel chatClient;

	@Transactional
	public ReviewResponse save(ReviewSaveRequset reviewSaveRequset) {

		Long userId = reviewSaveRequset.getUserId();

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 user가 없습니다."));

		Review review = reviewSaveRequset.toEntity(user);
		review.updateSentiment(analyzeReviewSentiment(review));
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
		review.updateSentiment(analyzeReviewSentiment(review));
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
	public List<ReviewListResponse> findPositiveReviewByContent(Long contentId) {
		return reviewRepository.findByContentIdAndSentimentIsPositive(contentId)
			.stream().map(ReviewListResponse::new)
			.toList();
	}

	@Transactional
	public List<ReviewListResponse> findNegativeReviewByContent(Long contentId) {
		return reviewRepository.findByContentIdAndSentimentIsNegative(contentId)
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

	public String analyzeReviewSentiment(Review entity) {

		var outputParser = new BeanOutputParser<>(String.class);

		String promptString = """
                리뷰: "{reviewText}"
                위 리뷰 정보가 긍정적인지 부정적인지 판단하고, 긍정인 경우에는 'positive'를, 부정인 경우에는 'negative'로 응답해줘.
                sentiment(응답)에는 'positive' 또는 'negative'만 올 수 있어. null이나 다른 값은 넣을 수 없어.
                'positive' 또는 'negative'를 넣기 애매한 경우에는 5점 만점인 {reviewRating}을 참고해서 응답해줘.
                예를들어 reviewText가 '애매해요'라면 'reviewRating'가 1점이면 'negative'이고 3점이면 'positive'로 응답해줘.
                처음에는 좋았다가 별로여서 수정하게 되거나 처음엔 별로였다가 좋아지는 경우가 생길 수 있으니 'reviewText'를 잘 분석해줘.
                {format}
                """;

		PromptTemplate template = new PromptTemplate(promptString, Map.of("reviewText", entity.getReviewContent(), "reviewRating", entity.getReviewRating(),"format", outputParser.getFormat()));
		Prompt prompt = template.create();
		Generation generation = chatClient.call(prompt).getResult();

		String sentiment = outputParser.parse(generation.getOutput().getContent());

		return sentiment;
	}
}
