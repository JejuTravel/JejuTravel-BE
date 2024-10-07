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
import com.example.jejutravel.domain.Dto.content.StayInfoResponse;
import com.example.jejutravel.domain.Dto.content.StayListResponse;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.service.OpenApiManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class StayController {

	@Value("${tourism.api.key}")
	private String apiKey;

	private final OpenApiManager openApiManager;

	@GetMapping("/stay")
	public ApiResponse<PageResponse<StayListResponse>> getStayList(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
		@RequestParam(value = "sigunguCode", defaultValue = "0") int sigunguCode,
		@RequestParam(value = "stayType", defaultValue = "") String stayType)  {

		String serviceKey = apiKey;
		String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
		String apiUrl = "";

		if(sigunguCode == 0 && stayType.equals("")){
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/searchStay1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&_type=json";
		} else if(sigunguCode != 0 && stayType.equals("")){
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/searchStay1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&sigunguCode=" + sigunguCode +
				"&_type=json";
		} else if(sigunguCode == 0 && !stayType.equals("")){
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/searchStay1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&" + stayType + "=1" +
				"&_type=json";
		}else if(sigunguCode != 0 && !stayType.equals("")){
			apiUrl = "https://apis.data.go.kr/B551011/ChsService1/searchStay1" +
				"?serviceKey=" + encodedServiceKey +
				"&numOfRows=10" +
				"&pageNo=" + pageNo +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&sigunguCode=" + sigunguCode +
				"&" + stayType + "=1" +
				"&_type=json";
		}
		log.info("{}", apiUrl);
		return ApiResponse.createSuccess(openApiManager.fetchStayList(apiUrl,pageNo));
	}

	@GetMapping("/stay/search")
	public ApiResponse<PageResponse<StayListResponse>> searchStayList(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
		@RequestParam(value = "keyword", defaultValue = "") String keyword)  {

		PageResponse<StayListResponse> response = null ;

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
			"&contentTypeId=80" +
			"&keyword=" + encodedKeyword +
			"&_type=json";

		response = openApiManager.fetchStayList(apiUrl, pageNo);

		if (response.getContent().isEmpty()) {
			return ApiResponse.createSuccessWithMessage(response,"No search results found.");
		}

		return ApiResponse.createSuccess(response);
	}

	@GetMapping("/stay/info/{contentId}")
	public ApiResponse<List<StayInfoResponse>> getStayInfo(@PathVariable Long contentId )  {

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
			"&contentTypeId=80" +
			"&_type=json" ;

		return ApiResponse.createSuccess(openApiManager.getStayInfo(apiUrl, apiUrl2));
	}
}
