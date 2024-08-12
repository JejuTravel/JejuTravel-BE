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

import com.example.jejutravel.global.PythonTranslator;
import com.example.jejutravel.global.api.ApiResponse;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class PublicToiletController {
    
	@Value("${tourism.api.key}")
	private String apiKey;

    @GetMapping("/publicToilet")
    public ApiResponse<?> callApi(        
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
    ){
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList";
            URI uri = new URI(apiUrl + "?serviceKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString()) 
            + "&pageNo=" +pageNo
            +"&numOfRows=5");
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

			List<Map<String, Object>> filteredData = new ArrayList<>();
            if (itemsNode.isArray()) {
                //데이터 필터링 및 반환: items 배열을 순회하며 필요한 정보를 Map에 저장하고, 이를 리스트로 반환했습니다.
                for (JsonNode dataNode : itemsNode) { 
                    Map<String, Object> newItem = new HashMap<>();
                    
                    // 한국어 -> 중국어 번역
                    String emdNmKo = dataNode.path("emdNm").asText();
                    String emdNmZh = PythonTranslator.translate(emdNmKo, "ko", "zh-cn");
                    newItem.put("emdNm", emdNmZh);
                    // newItem.put("emdNm", dataNode.path("emdNm").asText()); //읍면동 명

                    String rnAdresKo = dataNode.path("rnAdres").asText();
                    String rnAdresZh = PythonTranslator.translate(rnAdresKo, "ko", "zh-cn");
                    newItem.put("rnAdres", rnAdresZh);
                    // newItem.put("rnAdres", dataNode.path("rnAdres").asText()); //도로명주소
                    
                    String lnmAdresKo = dataNode.path("lnmAdres").asText();
                    String lnmAdresZh = PythonTranslator.translate(lnmAdresKo, "ko", "zh-cn");
                    newItem.put("lnmAdres", lnmAdresZh);
                    // newItem.put("lnmAdres", dataNode.path("lnmAdres").asText()); //지번 주소
                    
                    String toiletNmKo = dataNode.path("toiletNm").asText();
                    String toiletNmZh = PythonTranslator.translate(toiletNmKo, "ko", "zh-cn");
                    newItem.put("toiletNm", toiletNmZh);
                    // newItem.put("toiletNm", dataNode.path("toiletNm").asText()); //화장실 명
                    
                    String opnTimeInfoKo = dataNode.path("opnTimeInfo").asText();
                    String opnTimeInfoZh = PythonTranslator.translate(opnTimeInfoKo, "ko", "zh-cn");
                    newItem.put("opnTimeInfo", opnTimeInfoZh);
                    // newItem.put("opnTimeInfo", dataNode.path("opnTimeInfo").asText()); //개방 시간 정보 ex)연중무휴
                    
                    String filthPrcsMthdNmKo = dataNode.path("filthPrcsMthdNm").asText();
                    String filthPrcsMthdNmZh = PythonTranslator.translate(filthPrcsMthdNmKo, "ko", "zh-cn");
                    newItem.put("filthPrcsMthdNm", filthPrcsMthdNmZh);
                    // newItem.put("filthPrcsMthdNm", dataNode.path("filthPrcsMthdNm").asText()); //오물 처리 방식 명 ex)수세식
                    
                    newItem.put("laCrdnt", dataNode.path("laCrdnt").asText()); //위도 좌표
                    newItem.put("loCrdnt", dataNode.path("loCrdnt").asText()); //경도 좌표
                    newItem.put("telno", dataNode.path("telno").asText()); //전화번호
                    
                    newItem.put("toiletEntrncCctvInstlYn", dataNode.path("toiletEntrncCctvInstlYn").asText()); //화장실 입구 CCTV 설치 여부
                    newItem.put("diaperExhgTablYn", dataNode.path("diaperExhgTablYn").asText()); //기저귀 교환 탁자 여부

                    newItem.put("maleClosetCnt", dataNode.path("maleClosetCnt").asText()); //남성 대변기 수
                    newItem.put("maleUrinalCnt", dataNode.path("maleUrinalCnt").asText()); //남성 소변기 수
                    newItem.put("maleDspsnClosetCnt", dataNode.path("maleDspsnClosetCnt").asText()); //남성 장애인 대변기 수
                    newItem.put("maleDspsnUrinalCnt", dataNode.path("maleDspsnUrinalCnt").asText()); //남성 장애인 소변기 수
                    // newItem.put("maleChildClosetCnt", dataNode.path("maleChildClosetCnt").asText()); //남성 어린이 대변기 수
                    // newItem.put("maleChildUrinalCnt", dataNode.path("maleChildUrinalCnt").asText()); //남성 어린이 소변기 수
                    newItem.put("femaleClosetCnt", dataNode.path("femaleClosetCnt").asText()); //여성 대변기 수
                    newItem.put("femaleDspsnClosetCnt", dataNode.path("femaleDspsnClosetCnt").asText()); //여성 장애인 대변기 수
                    // newItem.put("femaleChildClosetCnt", dataNode.path("femaleChildClosetCnt").asText()); //여성 어린이 대변기 수
                    // newItem.put("etcCn", dataNode.path("etcCn").asText()); //기타 내용

                    // newItem.put("photo", dataNode.path("photo").asText()); //사진
                    newItem.put("photo", getPhotoUrls(dataNode.path("photo"))); // 사진 URL 리스트
                    
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
    
    // getPhotoUrls 메서드 추가
    private List<String> getPhotoUrls(JsonNode photoNode) {
        List<String> photoUrls = new ArrayList<>();
        if (photoNode.isArray()) {
            for (JsonNode urlNode : photoNode) {
                photoUrls.add(urlNode.asText());
            }
        }
        return photoUrls;
    }

	//publicToilet 검색
	@GetMapping("/publicToilet/search")
    public ApiResponse<?> searchPublicToilet(
        @RequestParam("toiletNm") String toiletNm ,//검색할 toiletNm 받아옴.
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
        ) { 
        StringBuilder result = new StringBuilder();
        try {
            // 중국어로 된 파라미터를 한국어로 번역
			String translatedToiletNm = PythonTranslator.translate(toiletNm, "zh-cn", "ko");
			// String translatedStationName = "공영주차장";

            
            String apiUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList";
            String encodedToiletNm = URLEncoder.encode(translatedToiletNm, StandardCharsets.UTF_8.toString()); //검색한 toiletName 인코딩
            // String encodedToiletNm = URLEncoder.encode(toiletNm, StandardCharsets.UTF_8.toString()); //검색한 toiletNm 인코딩
            URI uri = new URI(apiUrl + "?serviceKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString()) 
            + "&pageNo=" + pageNo
            +"&numOfRows=5" 
            + "&toiletNm=" 
            + encodedToiletNm);
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

            List<Map<String, Object>> filteredData = new ArrayList<>();
            if (itemsNode.isArray()) {
                 //데이터 필터링 및 반환: items 배열을 순회하며 필요한 정보를 Map에 저장하고, 이를 리스트로 반환했습니다.
                for (JsonNode dataNode : itemsNode) { 
                    Map<String, Object> newItem = new HashMap<>();
                    // 한국어 -> 중국어 번역
                    String emdNmKo = dataNode.path("emdNm").asText();
                    String emdNmZh = PythonTranslator.translate(emdNmKo, "ko", "zh-cn");
                    newItem.put("emdNm", emdNmZh);
                    // newItem.put("emdNm", dataNode.path("emdNm").asText()); //읍면동 명

                    String rnAdresKo = dataNode.path("rnAdres").asText();
                    String rnAdresZh = PythonTranslator.translate(rnAdresKo, "ko", "zh-cn");
                    newItem.put("rnAdres", rnAdresZh);
                    // newItem.put("rnAdres", dataNode.path("rnAdres").asText()); //도로명주소
                    
                    String lnmAdresKo = dataNode.path("lnmAdres").asText();
                    String lnmAdresZh = PythonTranslator.translate(lnmAdresKo, "ko", "zh-cn");
                    newItem.put("lnmAdres", lnmAdresZh);
                    // newItem.put("lnmAdres", dataNode.path("lnmAdres").asText()); //지번 주소
                    
                    String toiletNmKo = dataNode.path("toiletNm").asText();
                    String toiletNmZh = PythonTranslator.translate(toiletNmKo, "ko", "zh-cn");
                    newItem.put("toiletNm", toiletNmZh);
                    // newItem.put("toiletNm", dataNode.path("toiletNm").asText()); //화장실 명
                    
                    String opnTimeInfoKo = dataNode.path("opnTimeInfo").asText();
                    String opnTimeInfoZh = PythonTranslator.translate(opnTimeInfoKo, "ko", "zh-cn");
                    newItem.put("opnTimeInfo", opnTimeInfoZh);
                    // newItem.put("opnTimeInfo", dataNode.path("opnTimeInfo").asText()); //개방 시간 정보 ex)연중무휴
                    
                    String filthPrcsMthdNmKo = dataNode.path("filthPrcsMthdNm").asText();
                    String filthPrcsMthdNmZh = PythonTranslator.translate(filthPrcsMthdNmKo, "ko", "zh-cn");
                    newItem.put("filthPrcsMthdNm", filthPrcsMthdNmZh);
                    // newItem.put("filthPrcsMthdNm", dataNode.path("filthPrcsMthdNm").asText()); //오물 처리 방식 명 ex)수세식

                    // newItem.put("emdNm", dataNode.path("emdNm").asText()); //읍면동 명
                    // newItem.put("rnAdres", dataNode.path("rnAdres").asText()); //도로명주소
                    // newItem.put("lnmAdres", dataNode.path("lnmAdres").asText()); //지번 주소
                    // newItem.put("toiletNm", dataNode.path("toiletNm").asText()); //화장실 명
                    // newItem.put("opnTimeInfo", dataNode.path("opnTimeInfo").asText()); //개방 시간 정보 ex)연중무휴
                    // newItem.put("filthPrcsMthdNm", dataNode.path("filthPrcsMthdNm").asText()); //오물 처리 방식 명 ex)수세식
                    
                    newItem.put("laCrdnt", dataNode.path("laCrdnt").asText()); //위도 좌표
                    newItem.put("loCrdnt", dataNode.path("loCrdnt").asText()); //경도 좌표
                    newItem.put("telno", dataNode.path("telno").asText()); //전화번호
                    newItem.put("toiletEntrncCctvInstlYn", dataNode.path("toiletEntrncCctvInstlYn").asText()); //화장실 입구 CCTV 설치 여부
                    newItem.put("diaperExhgTablYn", dataNode.path("diaperExhgTablYn").asText()); //기저귀 교환 탁자 여부

                    newItem.put("maleClosetCnt", dataNode.path("maleClosetCnt").asText()); //남성 대변기 수
                    newItem.put("maleUrinalCnt", dataNode.path("maleUrinalCnt").asText()); //남성 소변기 수
                    newItem.put("maleDspsnClosetCnt", dataNode.path("maleDspsnClosetCnt").asText()); //남성 장애인 대변기 수
                    newItem.put("maleDspsnUrinalCnt", dataNode.path("maleDspsnUrinalCnt").asText()); //남성 장애인 소변기 수
                    // newItem.put("maleChildClosetCnt", dataNode.path("maleChildClosetCnt").asText()); //남성 어린이 대변기 수
                    // newItem.put("maleChildUrinalCnt", dataNode.path("maleChildUrinalCnt").asText()); //남성 어린이 소변기 수
                    newItem.put("femaleClosetCnt", dataNode.path("femaleClosetCnt").asText()); //여성 대변기 수
                    newItem.put("femaleDspsnClosetCnt", dataNode.path("femaleDspsnClosetCnt").asText()); //여성 장애인 대변기 수
                    // newItem.put("femaleChildClosetCnt", dataNode.path("femaleChildClosetCnt").asText()); //여성 어린이 대변기 수
                    // newItem.put("etcCn", dataNode.path("etcCn").asText()); //기타 내용

                    // newItem.put("photo", dataNode.path("photo").asText()); //사진
                    newItem.put("photo", getPhotoUrls(dataNode.path("photo"))); // 사진 URL 리스트
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
