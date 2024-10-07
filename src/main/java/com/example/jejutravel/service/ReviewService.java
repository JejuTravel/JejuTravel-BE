package com.example.jejutravel.service;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.jejutravel.domain.Dto.review.ReviewListResponse;
import com.example.jejutravel.domain.Dto.review.ReviewResponse;
import com.example.jejutravel.domain.Dto.review.ReviewSaveRequset;
import com.example.jejutravel.domain.Dto.review.ReviewUpdateRequest;
import com.example.jejutravel.domain.Entity.Review;
import com.example.jejutravel.domain.Entity.User;
import com.example.jejutravel.repository.ReviewRepository;
import com.example.jejutravel.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	private final OpenAiChatModel chatClient;

	// TreeMap을 사용하여 유저의 평점을 저장
	// 사용자별로 contentId와 rating(평점)을 저장하는 구조
	// TreeMap < userId 별로, TreeMap<contentId, rating> > 저장
	private TreeMap<Long, TreeMap<Long, Integer>> userRatingsMapTourism = new TreeMap<>();
	private TreeMap<Long, TreeMap<Long, Integer>> userRatingsMapShopping = new TreeMap<>();
	private TreeMap<Long, TreeMap<Long, Integer>> userRatingsMapStay = new TreeMap<>();
	private TreeMap<Long, TreeMap<Long, Integer>> userRatingsMapRestaurant = new TreeMap<>();

	@Transactional
	public ReviewResponse save(ReviewSaveRequset reviewSaveRequset) {

		Long userId = reviewSaveRequset.getUserId();

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 user가 없습니다."));

		Review review = reviewSaveRequset.toEntity(user);
		review.updateSentiment(analyzeReviewSentiment(review));
		reviewRepository.save(review);

		// 평점 데이터를 TreeMap에 추가
		/*
		Step 1. computeIfAbsent(userId, k -> new TreeMap<>())
			- userRatingsMap에서 userId에 해당하는 값(사용자의 contentId와 rating으로 이루어진 TreeMap)이 있는지 확인합니다.
			- 만약 해당 userId가 userRatingsMap에 없다면, 새로운 빈 TreeMap을 생성하고 userId를 키로 추가합니다.
			- 만약 이미 userId가 존재한다면, 해당 userId에 매핑된 기존의 TreeMap(contentId와 rating으로 이루어진 TreeMap)을 반환합니다.

		Step 2. put(review.getContentId(), review.getReviewRating())
			- 해당 사용자의 TreeMap에 contentId와 평점을 추가합니다.
			- contentId가 존재하지 않으면 새롭게 추가하고, 이미 존재하는 경우에는 기존 평점을 덮어씁니다.

		Ex)
		userRatingsMap = {
			1L -> { 101L -> 5, 102L -> 3 },  // 사용자 1의 콘텐츠 평점
			2L -> { 101L -> 4, 103L -> 2 }   // 사용자 2의 콘텐츠 평점
		};
		*/
		// contentTypeId에 따른 TreeMap 가져오기
		TreeMap<Long, TreeMap<Long, Integer>> selectedUserRatingsMap = getUserRatingsMap(reviewSaveRequset.getContentTypeId());
		// 해당 userId에 대한 TreeMap이 없다면 새로 생성하고 contentId와 평점을 추가
		selectedUserRatingsMap.computeIfAbsent(userId, k -> new TreeMap<>())
				.put(review.getContentId(), review.getReviewRating()); // contentId와 rating(평점) 추가

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

		// contentTypeId에 따른 TreeMap 가져오기
		TreeMap<Long, TreeMap<Long, Integer>> selectedUserRatingsMap = getUserRatingsMap(review.getContentTypeId());
		// 해당 userId에 대한 TreeMap이 없다면 새로 생성하고 contentId와 평점을 업데이트
		selectedUserRatingsMap.computeIfAbsent(review.getUser().getUserId(), k -> new TreeMap<>())
				.put(review.getContentId(), review.getReviewRating());

		ReviewResponse response = new ReviewResponse(review);

		return response;
	}

	@Transactional
	public void deleteReview(Long reviewId) {

		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException("해당 review가 없습니다."));

		review.updateReviewDeleteYn();
		reviewRepository.save(review);

		// TreeMap에 해당 review 정보
		Long userId = review.getUser().getUserId();
		Long contentId = review.getContentId();

		// contentTypeId에 따른 TreeMap 가져오기
		TreeMap<Long, TreeMap<Long, Integer>> selectedUserRatingsMap = getUserRatingsMap(review.getContentTypeId());
		// TreeMap에서 해당 userId와 contentId의 rating 제거
		if (selectedUserRatingsMap.containsKey(userId)) {
			selectedUserRatingsMap.get(userId).remove(contentId);
			// 만약 해당 userId에 더 이상 contentId가 없으면, userId 자체도 제거
			if (selectedUserRatingsMap.get(userId).isEmpty()) {
				selectedUserRatingsMap.remove(userId);
			}
		}
	}

	@Transactional
	public Page<ReviewListResponse> findByContentId(Long contentId, int pageNumber, Pageable pageable) {

		Page<Review> reviews = reviewRepository.findByContentId(contentId,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

		return  reviews.map(ReviewListResponse::new);
	}

	@Transactional
	public Page<ReviewListResponse> findPositiveReviewByContent(Long contentId, int pageNumber, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByContentIdAndSentimentIsPositive(contentId,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

		return  reviews.map(ReviewListResponse::new);
	}

	@Transactional
	public Page<ReviewListResponse> findNegativeReviewByContent(Long contentId, int pageNumber, Pageable pageable) {

		Page<Review> reviews = reviewRepository.findByContentIdAndSentimentIsNegative(contentId,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

		return  reviews.map(ReviewListResponse::new);
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
            리뷰 텍스트를 분석하여 감정을 판단합니다. 다음 지침을 정확히 따르세요:
            
            리뷰 내용: "{reviewText}"
            리뷰 평점 (5점 만점): "{reviewRating}"
            
            1. 리뷰 내용의 의미와 문맥을 최우선으로 고려하여 감정을 분석하세요.
                - 긍정적인 내용이면 'positive'로 응답하세요.
                - 부정적인 내용이면 'negative'로 응답하세요.
            2. 반환값에는 오직 'positive' 또는 'negative'만 올 수 있습니다. 다른 값, null, 또는 빈 값은 절대 허용되지 않습니다.
            3. 다음과 같은 중립적인 표현은 긍정으로 판단하세요: "괜찮다", "무난하다", "나쁘지 않다"
            4. 리뷰 내용이 명확하지 않거나, 긍정/부정의 판단이 어려운 애매한 표현인 경우에만 리뷰 평점을 참고하여 결정하세요:
                - 애매한 표현의 예시: "좋기도 하고 나쁘기도 해요", "잘 모르겠어요", "애매해요", "몰라요"
                - 위와 같은 애매한 표현에서는 평점을 참고하여 판단하세요:
                    - 평점이 1점이나 2점이면 'negative'로 판단하세요.
                    - 평점이 3점 이상이면 'positive'로 판단하세요.
            5. 리뷰 내용이 처음에는 긍정적이었다가 부정적으로 변하거나, 반대로 부정적이었다가 긍정적으로 변할 수 있습니다. 문맥을 전체적으로 분석하여 최종적인 감정을 결정하세요.        
            
            응답 형식은 반드시 다음과 같아야 합니다:
            'positive'
            또는
            'negative'
            
            모든 응답은 위의 규칙을 엄격히 따르세요. 형식을 정확히 지켜 응답을 생성해야 합니다.
            {format}
            """;

		PromptTemplate template = new PromptTemplate(promptString, Map.of("reviewText", entity.getReviewContent(), "reviewRating", entity.getReviewRating(),"format", outputParser.getFormat()));
		Prompt prompt = template.create();
		Generation generation = chatClient.call(prompt).getResult();
		log.info("generation {}",generation);

		String sentiment = outputParser.parse(generation.getOutput().getContent());

		log.info("sentiment {}",sentiment);

		return sentiment;
	}

	public List<String> getTop2Cat3ByUserId(Long userId) {
		List<Object[]> result = reviewRepository.findTop2Cat3ByUserId(userId);
		return result.stream()
			.map(record -> (String) record[0])
			.collect(Collectors.toList());
	}

	// 유저 평점 기반으로 유사한 유저 찾기
	public List<Long> findSimilarUsers(Long targetUserId, Long contentTypeId) {

		// contentTypeId에 따른 TreeMap 가져오기
		TreeMap<Long, TreeMap<Long, Integer>> selectedUserRatingsMap = getUserRatingsMap(contentTypeId);

		// targetUserId에 해당하는 평점을 가져옴
		TreeMap<Long, Integer> targetRatings = selectedUserRatingsMap.get(targetUserId);
		if (targetRatings == null) {
			throw new IllegalArgumentException("해당 유저의 평점 데이터를 찾을 수 없습니다.");
		}

		// 유사도를 계산하기 위한 메소드 (단순 유클리드 거리)
		List<Long> similarUsers = new ArrayList<>();
		double minDistance = Double.MAX_VALUE;

		for (Map.Entry<Long, TreeMap<Long, Integer>> entry : selectedUserRatingsMap.entrySet()) { // 순회하면서 다른 사용자들과의 평점을 비교
			if (!entry.getKey().equals(targetUserId)) { // 자신과의 비교는 필요 없으므로 targetUserId는 제외하고 진행
				// 공통된 콘텐츠에 대한 유클리드 거리 계산
				double distance = calculateEuclideanDistance(targetRatings, entry.getValue());

				// 가장 가까운 유저 찾기 (최소 거리)
				if (distance < minDistance) {
					minDistance = distance;
					similarUsers.clear();
					similarUsers.add(entry.getKey());
				} else if (distance == minDistance) {
					similarUsers.add(entry.getKey());
				}
			}
		}
//		System.out.println(targetUserId+" is similar with "+similarUsers+". And min distance is : "+minDistance);
		return similarUsers;
	}

	// 유클리드 거리 계산
	private double calculateEuclideanDistance(TreeMap<Long, Integer> ratings1, TreeMap<Long, Integer> ratings2) {
		double sum = 0.0;

		// 두 사용자 모두 평가한 공통된 콘텐츠에 대해서만 계산
		for (Map.Entry<Long, Integer> entry : ratings1.entrySet()) {
			Long contentId = entry.getKey();
			if (ratings2.containsKey(contentId)) {
				sum += Math.pow(entry.getValue() - ratings2.get(contentId), 2);
			}
		}

		return Math.sqrt(sum);  // 유클리드 거리 반환
	}

public Set<Long> findRecommendedContentIds(Long targetUserId, Long contentTypeId) {
	// 유사한 사용자 목록 도출
	List<Long> similarUserIds = findSimilarUsers(targetUserId, contentTypeId);

	// 선택한 contentTypeId에 맞는 TreeMap 선택
	TreeMap<Long, TreeMap<Long, Integer>> selectedUserRatingsMap = getUserRatingsMap(contentTypeId);

	// targetUserId가 리뷰한 contentId 목록 가져오기
	TreeMap<Long, Integer> targetUserRatings = selectedUserRatingsMap.get(targetUserId);
	Set<Long> targetReviewedContentIds = (targetUserRatings != null) ? targetUserRatings.keySet() : new HashSet<>();

	// 유사한 사용자가 리뷰한 contentId 중, 평점이 3점 이상이고 sentiment가 positive인, targetUser가 리뷰하지 않은 contentId 도출
	Set<Long> recommendedContentIds = new HashSet<>();
	for (Long similarUserId : similarUserIds) {
		TreeMap<Long, Integer> similarUserRatings = selectedUserRatingsMap.get(similarUserId);
		if (similarUserRatings != null) {
			for (Long contentId : similarUserRatings.keySet()) {
				Integer rating = similarUserRatings.get(contentId);

				// 리뷰 객체에서 평점과 감정 상태 확인
				Review review = reviewRepository.findByUserIdAndContentId(similarUserId, contentId);
				if (review != null && rating >= 3 && review.getSentiment().equals("positive")) {
					if (!targetReviewedContentIds.contains(contentId)) {
						recommendedContentIds.add(contentId);  // 추천할 contentId 추가
					}
				}
			}
		}
	}

	return recommendedContentIds;
}

	// contentTypeId에 따라 적절한 userRatingsMap을 선택하는 메서드
	private TreeMap<Long, TreeMap<Long, Integer>> getUserRatingsMap(Long contentTypeId) {
		if (contentTypeId == 76) { // tourism
			return userRatingsMapTourism;
		} else if (contentTypeId == 79) { // shopping
			return userRatingsMapShopping;
		} else if (contentTypeId == 80) { // stay
			return userRatingsMapStay;
		} else if (contentTypeId == 82) { // restaurant
			return userRatingsMapRestaurant;
		} else {
			throw new IllegalArgumentException("올바른 contentTypeId를 제공해야 합니다.");
		}
	}

	public List<Map<String, Object>> getInfoForContentIds(List<Long> contentIds, Long contentTypeId) {
		RestTemplate restTemplate = new RestTemplate();
		List<Map<String, Object>> infoResponses = new ArrayList<>();

		String endpoint;
		if (contentTypeId == 79L) {
			endpoint = "/shopping/info/";
		} else if (contentTypeId == 76L) {
			endpoint = "/tourism/info/";
		} else if (contentTypeId == 80L) {
			endpoint = "/stay/info/";
		} else if (contentTypeId == 82L) {
			endpoint = "/restaurant/info/";
		} else {
			throw new IllegalArgumentException("올바른 contentTypeId를 제공해야 합니다.");
		}

		for (Long contentId : contentIds) {
			String apiUrl = "http://localhost:8080/api/v1" + endpoint + contentId;
			ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

			if (response.getBody() != null) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					Map<String, Object> result = mapper.readValue(response.getBody(), Map.class);
					infoResponses.add(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return infoResponses;
	}

	// 애플리케이션이 시작될 때, DB에 있는 모든 리뷰 데이터를 불러와 userRatingsMap을 초기화합니다.
	//TreeMap<Long, TreeMap<Long, Integer>> 구조로 사용자별 평점 데이터를 저장합니다.
	@PostConstruct
	public void initializeUserRatingsMap() {
		List<Review> reviews = reviewRepository.findAll();
		for (Review review : reviews) {
			// 삭제되지 않은 리뷰만 TreeMap에 추가
			if (!review.isReviewDeleteYn()) {
				Long userId = review.getUser().getUserId();
				Long contentId = review.getContentId();
				Integer rating = review.getReviewRating();
				Long contentTypeId = review.getContentTypeId();

				// contentTypeId에 따라 적절한 userRatingsMap에 데이터 저장
				if (contentTypeId == 76) { // tourism
					userRatingsMapTourism.computeIfAbsent(userId, k -> new TreeMap<>())
							.put(contentId, rating);
				} else if (contentTypeId == 79) { // shopping
					userRatingsMapShopping.computeIfAbsent(userId, k -> new TreeMap<>())
							.put(contentId, rating);
				} else if (contentTypeId == 80) { // stay
					userRatingsMapStay.computeIfAbsent(userId, k -> new TreeMap<>())
							.put(contentId, rating);
				} else if (contentTypeId == 82) { // restaurant
					userRatingsMapRestaurant.computeIfAbsent(userId, k -> new TreeMap<>())
							.put(contentId, rating);
				}
			}
		}
//		System.out.println("PostConstruct TreeMap data for Tourism: " + userRatingsMapTourism);
//		System.out.println("PostConstruct TreeMap data for Shopping: " + userRatingsMapShopping);
//		System.out.println("PostConstruct TreeMap data for Stay: " + userRatingsMapStay);
//		System.out.println("PostConstruct TreeMap data for Restaurant: " + userRatingsMapRestaurant);
	}

}