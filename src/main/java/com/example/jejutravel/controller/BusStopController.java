package com.example.jejutravel.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
// import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.global.PythonTranslator;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class BusStopController {
    
    @Value("${jeju.api.key}")
	private String jejuApiKey;

    @GetMapping("/busStop")
    public ApiResponse<?> callApi(
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
		){
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/" +
            jejuApiKey + "?" +
            "number=" + pageNo +
            "&limit=5";

            URL url = new URL(apiUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
			if (urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));
			}
            String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
            urlConnection.disconnect();

			 // JSON 파싱 및 재구성
			ObjectMapper objectMapper = new ObjectMapper();
			 JsonNode rootNode = objectMapper.readTree(result.toString()); //result 받아옴
			 JsonNode dataArray = rootNode.path("data"); //각 내용들

			List<Map<String, String>> filteredData = new ArrayList<>();
			for (JsonNode dataNode : dataArray) {
				Map<String, String> newItem = new HashMap<>();
				newItem.put("stationId", dataNode.path("stationId").asText());
				newItem.put("longitude", dataNode.path("longitude").asText());
				newItem.put("latitude", dataNode.path("latitude").asText());
				// newItem.put("stationType", dataNode.path("stationType").asText()); //stationType 의미있지 않을 듯. ex)"기타" / "현대카드" / "신형 / "각주형" / "일반형" 등

				// 한국어 -> 중국어 번역
				String stationNameKo = dataNode.path("stationName").asText();
				String stationNameZh = PythonTranslator.translate(stationNameKo, "ko", "zh-cn");
				newItem.put("stationName", stationNameZh);
				// newItem.put("stationName", dataNode.path("stationName").asText());	
				
				String stationAddressKo = dataNode.path("stationAddress").asText();
            	String stationAddressZh = PythonTranslator.translate(stationAddressKo, "ko", "zh-cn");
				newItem.put("stationAddress", stationAddressZh);
				// newItem.put("stationAddress", dataNode.path("stationAddress").asText());
				
				String localInfoKo = dataNode.path("localInfo").asText();
            	String localInfoZh = PythonTranslator.translate(localInfoKo, "ko", "zh-cn");
				newItem.put("localInfo", localInfoZh);
				// newItem.put("localInfo", dataNode.path("localInfo").asText());
				
				String directionKo = dataNode.path("direction").asText();
            	String directionZh = PythonTranslator.translate(directionKo, "ko", "zh-cn");
				newItem.put("direction", directionZh);
				// newItem.put("direction", dataNode.path("direction").asText());

				filteredData.add(newItem);
			}

  			 // 객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(filteredData, "버스 정류소 목록 조회 성공했습니다.");

        }catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.createError("(버스정류소 /busStop)API 호출 중 오류가 발생했습니다.");
		}
    }

	//busStop 검색
	@GetMapping("/busStop/search")
    public ApiResponse<?> searchBusStop( //검색할 stationName 받아옴.
		@RequestParam("stationName") String stationName,
		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo
		) {
        StringBuilder result = new StringBuilder();
        try {
			// 중국어로 된 파라미터를 한국어로 번역
			String translatedStationName = PythonTranslator.translate(stationName, "zh-cn", "ko");
			// String translatedStationName = "한국병원";
 
			String encodedStationName = URLEncoder.encode(translatedStationName, StandardCharsets.UTF_8.toString()); // 번역된 StationName 인코딩
			// String encodedStationName = URLEncoder.encode(stationName, StandardCharsets.UTF_8.toString()); //검색한 stationName 인코딩
            String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/" +
            jejuApiKey + "?" +
			"stationName=" + encodedStationName +						
            "&number=" + pageNo +
            "&limit=5";

            URL url = new URL(apiUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
			if (urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
			} else {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), StandardCharsets.UTF_8));
			}
            String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
            urlConnection.disconnect();

			 // JSON 파싱 및 재구성
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(result.toString()); //result 받아옴
			JsonNode dataArray = rootNode.path("data"); //각 내용들

			List<Map<String, String>> filteredData = new ArrayList<>();
			for (JsonNode dataNode : dataArray) {
				Map<String, String> newItem = new HashMap<>();

				newItem.put("stationId", dataNode.path("stationId").asText());
				newItem.put("longitude", dataNode.path("longitude").asText());
				newItem.put("latitude", dataNode.path("latitude").asText());
				// newItem.put("stationType", dataNode.path("stationType").asText()); //stationType 의미있지 않을 듯. ex)"기타" / "현대카드" / "신형 / "각주형" / "일반형" 등

				
				// 한국어 -> 중국어 번역
				String stationNameKo = dataNode.path("stationName").asText();
				String stationNameZh = PythonTranslator.translate(stationNameKo, "ko", "zh-cn");
				newItem.put("stationName", stationNameZh);
				// newItem.put("stationName", dataNode.path("stationName").asText());
				
				String stationAddressKo = dataNode.path("stationAddress").asText();
            	String stationAddressZh = PythonTranslator.translate(stationAddressKo, "ko", "zh-cn");
				newItem.put("stationAddress", stationAddressZh);
				// newItem.put("stationAddress", dataNode.path("stationAddress").asText());
				
				String localInfoKo = dataNode.path("localInfo").asText();
            	String localInfoZh = PythonTranslator.translate(localInfoKo, "ko", "zh-cn");
				newItem.put("localInfo", localInfoZh);
				// newItem.put("localInfo", dataNode.path("localInfo").asText());

				String directionKo = dataNode.path("direction").asText();
            	String directionZh = PythonTranslator.translate(directionKo, "ko", "zh-cn");
				newItem.put("direction", directionZh);
				// newItem.put("direction", dataNode.path("direction").asText());

				filteredData.add(newItem);
			}

  			// 객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(filteredData, "버스 정류소 목록 검색 성공했습니다.");

        }catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.createError("(버스정류소 /busStop)API 호출 중 오류가 발생했습니다.");
		}
    }
}
