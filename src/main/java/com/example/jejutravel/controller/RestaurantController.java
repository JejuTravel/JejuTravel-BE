package com.example.jejutravel.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jejutravel.global.api.PageResponse;
import com.example.jejutravel.domain.Dto.content.ContentListResponse;
import com.example.jejutravel.domain.Dto.content.RestaurantInfoResponse;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.service.OpenApiManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestaurantController {

	@Value("${tourism.api.key}")
	private String apiKey;

	private final OpenApiManager openApiManager;

	@GetMapping("/restaurant")
	public ApiResponse<PageResponse<ContentListResponse>> getRestaurantList(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
		@RequestParam(value = "sigunguCode", defaultValue = "0") int sigunguCode)  {

		String serviceKey = apiKey;
		String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
		String apiUrl = "";

		if(sigunguCode == 0){
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/areaBasedList1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&contentTypeId=82" +
				"&_type=json";
		} else {
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/areaBasedList1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&contentTypeId=82" +
				"&sigunguCode=" + sigunguCode +
				"&_type=json";
		}
		return ApiResponse.createSuccess(openApiManager.fetchContentList(apiUrl,pageNo));
	}

	@GetMapping("/restaurant/search")
	public ApiResponse<PageResponse<ContentListResponse>> searhRestaurantList(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
		@RequestParam(value = "keyword", defaultValue = "") String keyword)  {

		PageResponse<ContentListResponse> response = null ;

		if(keyword.equals("")){
			return ApiResponse.createSuccessWithMessage(response,"검색어를 입력해 주세요.");
		}

		String serviceKey = apiKey;
		String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		String apiUrl = "https://apis.data.go.kr/B551011/ChsService1/searchKeyword1" +
			"?serviceKey=" + encodedServiceKey +
			"&numOfRows=10" +
			"&pageNo=" + pageNo +
			"&MobileOS=ETC" +
			"&MobileApp=JejuTravel" +
			"&areaCode=39" +
			"&contentTypeId=82" +
			"&keyword=" + encodedKeyword +
			"&_type=json";

		response = openApiManager.fetchContentList(apiUrl, pageNo);

		if (response.getContent().isEmpty()) {
			return ApiResponse.createSuccessWithMessage(response,"No search results found.");
		}

		return ApiResponse.createSuccess(response);
	}

	@GetMapping("/restaurant/info/{contentId}")
	public ApiResponse<List<RestaurantInfoResponse>> getRestaurantInfo(@PathVariable Long contentId )  {

		String serviceKey = apiKey;
		String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
		String apiUrl = "https://apis.data.go.kr/B551011/ChsService1/detailCommon1" +
			"?serviceKey=" + encodedServiceKey +
			"&numOfRows=10" +
			"&pageNo=1"  +
			"&MobileOS=ETC" +
			"&MobileApp=JejuTravel" +
			"&contentId=" + contentId +
			"&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&catcodeYN=Y" +
			"&_type=json" ;

		String apiUrl2 = "https://apis.data.go.kr/B551011/ChsService1/detailIntro1" +
			"?serviceKey=" + encodedServiceKey +
			"&numOfRows=10" +
			"&pageNo=1"  +
			"&MobileOS=ETC" +
			"&MobileApp=JejuTravel" +
			"&contentId=" + contentId +
			"&contentTypeId=82" +
			"&_type=json" ;

		return ApiResponse.createSuccess(openApiManager.getRestaurantInfo(apiUrl, apiUrl2));
	}
}
