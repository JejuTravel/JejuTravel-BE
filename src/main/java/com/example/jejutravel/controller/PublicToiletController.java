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
import com.example.jejutravel.domain.Dto.content.PublicToiletResponse;
@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class PublicToiletController {
    
	@Value("${tourism.api.key}")
	private String apiKey;
    private final JejuApiManager jejuApiManager;

    @GetMapping("/publicToilet")
    public ApiResponse<PageResponse<PublicToiletResponse>> callApi(
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
    ){
        String serviceKey = apiKey;
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        try {
            String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList"
                    + "?serviceKey=" + encodedServiceKey
                    + "&pageNo=" +pageNo
                    + "&numOfRows=5";

            // getApiResponse 메서드를 JejuApiManager에서 호출
            String response = jejuApiManager.getApiResponse(apiUrl);

  			//  객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchPublicToilet(response, pageNo), "공중화장실 목록 조회 성공했습니다.");
        
        }catch (Exception e) {
			e.printStackTrace();
			return (ApiResponse<PageResponse<PublicToiletResponse>>) ApiResponse.createError("(공중화장실 /publicToilet)API 호출 중 오류가 발생했습니다.");
        }
    }

	//publicToilet 검색
	@GetMapping("/publicToilet/search")
    public ApiResponse<PageResponse<PublicToiletResponse>> searchPublicToilet(
        @RequestParam("toiletNm") String toiletNm ,//검색할 toiletNm 받아옴.
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
        ) { 

            String serviceKey = apiKey;

            try {
                // 중국어로 된 파라미터를 한국어로 번역
                String translatedToiletNm = PythonTranslator.translate(toiletNm, "zh-cn", "ko");
                String encodedToiletNm = URLEncoder.encode(translatedToiletNm, StandardCharsets.UTF_8.toString()); //검색한 toiletName 인코딩


                String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList"
                        + "?serviceKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString())
                        + "&pageNo=" + pageNo
                        +"&numOfRows=5"
                        + "&toiletNm="+ encodedToiletNm;

                // getApiResponse 메서드를 JejuApiManager에서 호출
                String response = jejuApiManager.getApiResponse(apiUrl);

                 // 객체를 직접 반환
                return ApiResponse.createSuccessWithMessage(jejuApiManager.fetchPublicToilet(response, pageNo), "공중화장실 목록 검색 성공했습니다.");

            }catch (Exception e) {
                e.printStackTrace();
                return (ApiResponse<PageResponse<PublicToiletResponse>>) ApiResponse.createError("(공중화장실 /publicToilet)API 호출 중 오류가 발생했습니다.");
            }
    }
}
