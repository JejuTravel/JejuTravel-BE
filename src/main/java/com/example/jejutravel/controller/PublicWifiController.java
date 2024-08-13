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

import com.example.jejutravel.global.PythonTranslator;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.global.api.PageResponse;
import com.example.jejutravel.service.JejuApiManager;
import com.example.jejutravel.domain.Dto.content.PublicWifiResponse;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class PublicWifiController {
    
    @Value("${jeju.api.key}")
	private String jejuApiKey;
	private final JejuApiManager jejuApiManager;

    @GetMapping("/publicWifi")
    public ApiResponse<PageResponse<PublicWifiResponse>> callApi(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
	){
		String serviceKey = jejuApiKey;

		try {
			String apiUrl = "https://open.jejudatahub.net/api/proxy/Dtb18ta1btbD1Da1a81aaDttab6tDabb/" +
            serviceKey + "?" +
            "number=" + pageNo +
            "&limit=5";

			// getApiResponse 메서드를 JejuApiManager에서 호출
			String response = jejuApiManager.getApiResponse(apiUrl);

  			 // 객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchPublicWifi(response, pageNo), "공공와이파이 목록 조회 성공했습니다.");

        }catch (Exception e) {
			e.printStackTrace();
			return (ApiResponse<PageResponse<PublicWifiResponse>>)ApiResponse.createError("(공공와이파이 /publicWifi)API 호출 중 오류가 발생했습니다.");
		}
    }

	//publicWifi 검색
	@GetMapping("/publicWifi/search")
    public ApiResponse<PageResponse<PublicWifiResponse>> searchPublicWifi(
		@RequestParam("apGroupName") String apGroupName, //검색할 apGroupName 받아옴.
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
		) {

			String serviceKey = jejuApiKey;

			try {
				// 중국어로 된 파라미터를 한국어로 번역
				String translatedApGroupName = PythonTranslator.translate(apGroupName, "zh-cn", "ko");
				String encodedApGroupName = URLEncoder.encode(translatedApGroupName, StandardCharsets.UTF_8.toString()); //검색한 apGroupName 인코딩

				String apiUrl = "https://open.jejudatahub.net/api/proxy/Dtb18ta1btbD1Da1a81aaDttab6tDabb/" +
				serviceKey + "?" +
				"apGroupName=" + encodedApGroupName +
				"&number=" + pageNo +
				"&limit=5";

				// getApiResponse 메서드를 JejuApiManager에서 호출
				String response = jejuApiManager.getApiResponse(apiUrl);

				 // 객체를 직접 반환
				return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchPublicWifi(response, pageNo), "공공와이파이 목록 검색 성공했습니다.");

			}catch (Exception e) {
				e.printStackTrace();
				return (ApiResponse<PageResponse<PublicWifiResponse>>) ApiResponse.createError("(공공와이파이 /publicWifi)API 호출 중 오류가 발생했습니다.");
			}
    }
}
