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

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class BusStopController {
    
    @Value("${jeju.api.key}")
	private String jejuApiKey;

    @GetMapping("/busStop")
    public ApiResponse<?> callApi(){
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/" +
            jejuApiKey + "?" +
            "number=1" +
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
				newItem.put("stationName", dataNode.path("stationName").asText());
				newItem.put("stationAddress", dataNode.path("stationAddress").asText());
				newItem.put("localInfo", dataNode.path("localInfo").asText());
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
    public ApiResponse<?> searchBusStop(@RequestParam("stationName") String stationName) {//검색할 stationName 받아옴.
        StringBuilder result = new StringBuilder();
        try {
			String encodedStationName = URLEncoder.encode(stationName, StandardCharsets.UTF_8.toString()); //검색한 stationName 인코딩
            String apiUrl = "https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/" +
            jejuApiKey + "?" +
			"stationName=" + encodedStationName +						
            "&number=1" +
            "&limit=100";

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
				newItem.put("stationName", dataNode.path("stationName").asText());
				newItem.put("stationAddress", dataNode.path("stationAddress").asText());
				newItem.put("localInfo", dataNode.path("localInfo").asText());
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
