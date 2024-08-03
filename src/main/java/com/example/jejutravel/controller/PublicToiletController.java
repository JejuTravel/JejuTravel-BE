package com.example.jejutravel.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
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

public class PublicToiletController {
    
	@Value("${tourism.api.key}")
	private String apiKey;

    @GetMapping("/publicToilet")
    public ApiResponse<?> callApi(){
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList";
            URI uri = new URI(apiUrl + "?serviceKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString()) + "&pageNo=1&numOfRows=3");
            URL url = uri.toURL();
			
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
            JsonNode rootNode = objectMapper.readTree(result.toString());
            //단계별로 접근하여, 원하는 데이터에 정확하게 접근
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

			List<Map<String, String>> filteredData = new ArrayList<>();
            if (itemsNode.isArray()) {
                //데이터 필터링 및 반환: items 배열을 순회하며 필요한 정보를 Map에 저장하고, 이를 리스트로 반환했습니다.
                for (JsonNode dataNode : itemsNode) { 
                    Map<String, String> newItem = new HashMap<>();
                    newItem.put("emdNm", dataNode.path("emdNm").asText());
                    newItem.put("rnAdres", dataNode.path("rnAdres").asText());
                    newItem.put("lnmAdres", dataNode.path("lnmAdres").asText());
                    newItem.put("toiletNm", dataNode.path("toiletNm").asText());
                    newItem.put("opnTimeInfo", dataNode.path("opnTimeInfo").asText());
                    newItem.put("photo", dataNode.path("photo").asText());
                    filteredData.add(newItem);
                }
            }

  			//  객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(filteredData, "공중화장실 목록 조회 성공했습니다.");
        
        }catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.createError("(공중화장실 /publicToilet)API 호출 중 오류가 발생했습니다.");
        }
    }
    

	//publicToilet 검색
	@GetMapping("/publicToilet/search")
    public ApiResponse<?> searchPublicToilet(@RequestParam("toiletNm") String toiletNm) { //검색할 toiletNm 받아옴.
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList";
			String encodedToiletNm = URLEncoder.encode(toiletNm, StandardCharsets.UTF_8.toString()); //검색한 toiletNm 인코딩
            URI uri = new URI(apiUrl + "?serviceKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString()) 
            + "&pageNo=1&numOfRows=100" + "&toiletNm=" + encodedToiletNm);
            URL url = uri.toURL();

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
			JsonNode rootNode = objectMapper.readTree(result.toString());
			 //단계별로 접근하여, 원하는 데이터에 정확하게 접근
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<Map<String, String>> filteredData = new ArrayList<>();
            if (itemsNode.isArray()) {
                 //데이터 필터링 및 반환: items 배열을 순회하며 필요한 정보를 Map에 저장하고, 이를 리스트로 반환했습니다.
                for (JsonNode dataNode : itemsNode) { 
                    Map<String, String> newItem = new HashMap<>();
                    newItem.put("emdNm", dataNode.path("emdNm").asText());
                    newItem.put("rnAdres", dataNode.path("rnAdres").asText());
                    newItem.put("lnmAdres", dataNode.path("lnmAdres").asText());
                newItem.put("toiletNm", dataNode.path("toiletNm").asText());
                newItem.put("opnTimeInfo", dataNode.path("opnTimeInfo").asText());
                newItem.put("photo", dataNode.path("photo").asText());
                filteredData.add(newItem);
                }
            }

  			 // 객체를 직접 반환
			return ApiResponse.createSuccessWithMessage(filteredData, "공중화장실 목록 검색 성공했습니다.");

        }catch (Exception e) {
			e.printStackTrace();
			return ApiResponse.createError("(공중화장실 /publicToilet)API 호출 중 오류가 발생했습니다.");
		}
    }
}
