package com.example.jejutravel.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.global.PythonTranslator;
import com.example.jejutravel.global.api.PageResponse;
import com.example.jejutravel.service.JejuApiManager;
import com.example.jejutravel.domain.Dto.content.BusStopResponse;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class BusStopController {
    
    @Value("${jeju.api.key}")
	private String jejuApiKey;
	private final JejuApiManager jejuApiManager;

    @GetMapping("/busStop")
    public ApiResponse<PageResponse<BusStopResponse>> callApi(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
		){
			String serviceKey = jejuApiKey;

			try {
				String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/" +
				serviceKey +
				"?number=" + pageNo +
				"&limit=5";

				// getApiResponse 메서드를 JejuApiManager에서 호출
				String response = jejuApiManager.getApiResponse(apiUrl);

				 // 객체를 직접 반환
				return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchBusStop(response, pageNo), "버스 정류소 목록 조회 성공했습니다.");

			}catch (Exception e) {
				e.printStackTrace();
				return (ApiResponse<PageResponse<BusStopResponse>>) ApiResponse.createError("(버스정류소 /busStop)API 호출 중 오류가 발생했습니다.");
			}
    	}

	//busStop 검색
	@GetMapping("/busStop/search")
    public ApiResponse<PageResponse<BusStopResponse>> searchBusStop(
		@RequestParam("stationName") String stationName, //검색할 stationName 받아옴.
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
		) {
			String serviceKey = jejuApiKey;

			try {
				// 중국어로 된 파라미터를 한국어로 번역
				String translatedStationName = PythonTranslator.translate(stationName, "zh-cn", "ko");
				String encodedStationName = URLEncoder.encode(translatedStationName, StandardCharsets.UTF_8.toString()); // 번역된 StationName 인코딩

				String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/"
				+ serviceKey + "?" +
				"stationName=" + encodedStationName +
				"&number=" + pageNo +
				"&limit=5";

				// getApiResponse 메서드를 JejuApiManager에서 호출
				String response = jejuApiManager.getApiResponse(apiUrl);

				// 객체를 직접 반환
				return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchBusStop(response, pageNo), "버스 정류소 목록 검색 성공했습니다.");

			}catch (Exception e) {
				e.printStackTrace();
				return (ApiResponse<PageResponse<BusStopResponse>>) ApiResponse.createError("(버스정류소 /busStop)API 호출 중 오류가 발생했습니다.");
			}
    	}
}
