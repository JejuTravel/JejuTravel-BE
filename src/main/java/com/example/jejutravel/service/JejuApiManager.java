package com.example.jejutravel.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.jejutravel.global.PythonTranslator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import com.example.jejutravel.domain.Dto.content.*;
import com.example.jejutravel.global.api.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JejuApiManager {

    public String getApiResponse(String apiUrl) throws Exception {
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

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        urlConnection.disconnect();

        return result.toString();
    }

    public PageResponse<BusStopResponse> fetchBusStop(String apiUrl, int pageNo) throws JsonProcessingException {
        List<BusStopResponse> result = new ArrayList<>();
        int numOfRows = 5; // 한 페이지에 5개 data 씩 표출.
        int totalCount = 0; // 전체 data 개수.

        try {
            // JSON 파싱 및 재구성
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(apiUrl.toString()); //버스 정류소 제공 api에서 결과 받아옴

            JsonNode dataArray = rootNode.path("data"); //결과의 data 부분
            totalCount = rootNode.path("totCnt").asInt(); // 결과의 totCnt 부분을 int로 가져오기

            //검색 결과가 없을 경우
            if (dataArray.isEmpty()) {
                return new PageResponse<>(numOfRows, pageNo, totalCount, result);
            }
            //검색 결과가 있는 경우
            for (JsonNode dataNode : dataArray) {
                BusStopResponse dto = BusStopResponse.builder()
                        .stationId(dataNode.path("stationId").asText())
                        .longitude(dataNode.path("longitude").asText())
                        .latitude(dataNode.path("latitude").asText())
                        .stationName(PythonTranslator.translate(dataNode.path("stationName").asText(), "ko", "zh-cn"))
                        .stationAddress(PythonTranslator.translate(dataNode.path("stationAddress").asText(), "ko", "zh-cn"))
                        .localInfo(PythonTranslator.translate(dataNode.path("localInfo").asText(), "ko", "zh-cn"))
                        .direction(PythonTranslator.translate(dataNode.path("direction").asText(), "ko", "zh-cn"))
                        .build();

                result.add(dto);
            }

            return new PageResponse<>(numOfRows, pageNo, totalCount, result);
        } catch(Exception e){
            e.printStackTrace();
        }
        return new PageResponse<>(numOfRows, pageNo, totalCount, result);
    }
    public PageResponse<PublicWifiResponse> fetchPublicWifi(String apiUrl, int pageNo) throws JsonProcessingException {
        List<PublicWifiResponse> result = new ArrayList<>();
        int numOfRows = 5; // 한 페이지에 5개 data 씩 표출.
        int totalCount = 0; // 전체 data 개수.

        try {
            // JSON 파싱 및 재구성
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(apiUrl.toString()); //공공와이파이 제공 api에서 결과 받아옴

            JsonNode dataArray = rootNode.path("data"); //결과의 data 부분
            totalCount = rootNode.path("totCnt").asInt(); // 결과의 totCnt 부분을 int로 가져오기

            //검색 결과가 없을 경우
            if (dataArray.isEmpty()) {
                return new PageResponse<>(numOfRows, pageNo, totalCount, result);
            }
            //검색 결과가 있는 경우
            for (JsonNode dataNode : dataArray) {
                PublicWifiResponse dto = PublicWifiResponse.builder()
                        .longitude(dataNode.path("longitude").asText())
                        .latitude(dataNode.path("latitude").asText())
                        .apGroupName(PythonTranslator.translate(dataNode.path("apGroupName").asText(), "ko", "zh-cn"))
                        .installLocationDetail(PythonTranslator.translate(dataNode.path("installLocationDetail").asText(), "ko", "zh-cn"))
                        .category(PythonTranslator.translate(dataNode.path("category").asText(), "ko", "zh-cn"))
                        .categoryDetail(PythonTranslator.translate(dataNode.path("categoryDetail").asText(), "ko", "zh-cn"))
                        .addressDong(PythonTranslator.translate(dataNode.path("addressDong").asText(), "ko", "zh-cn"))
                        .addressDetail(PythonTranslator.translate(dataNode.path("addressDetail").asText(), "ko", "zh-cn"))
                        .build();

                result.add(dto);
            }

            return new PageResponse<>(numOfRows, pageNo, totalCount, result);
        } catch(Exception e){
            e.printStackTrace();
        }
        return new PageResponse<>(numOfRows, pageNo, totalCount, result);
    }

    public PageResponse<PublicToiletResponse> fetchPublicToilet(String apiUrl, int pageNo) throws JsonProcessingException {
        List<PublicToiletResponse> result = new ArrayList<>();
        int numOfRows = 5; // 한 페이지에 5개 data 씩 표출.
        int totalCount = 0; // 전체 data 개수.

        try {
            // JSON 파싱 및 재구성
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(apiUrl.toString()); //공공와이파이 제공 api에서 결과 받아옴

            JsonNode dataArray = rootNode.path("response").path("body").path("items").path("item"); //결과의 item 부분
            totalCount = rootNode.path("response").path("body").path("totalCount").asInt(); // 결과의 total count 부분을 int로 가져오기

            //검색 결과가 없을 경우
            if (dataArray.isEmpty()) {
                return new PageResponse<>(numOfRows, pageNo, totalCount, result);
            }
            //검색 결과가 있는 경우
            for (JsonNode dataNode : dataArray) {
                PublicToiletResponse dto = PublicToiletResponse.builder()
                        .laCrdnt(dataNode.path("laCrdnt").asText())
                        .loCrdnt(dataNode.path("loCrdnt").asText())
                        .telno(dataNode.path("telno").asText())
                        .toiletEntrncCctvInstlYn(dataNode.path("toiletEntrncCctvInstlYn").asText())
                        .diaperExhgTablYn(dataNode.path("diaperExhgTablYn").asText())
                        .maleClosetCnt(dataNode.path("maleClosetCnt").asText())
                        .maleUrinalCnt(dataNode.path("maleUrinalCnt").asText())
                        .maleDspsnClosetCnt(dataNode.path("maleDspsnClosetCnt").asText())
                        .maleDspsnUrinalCnt(dataNode.path("maleDspsnUrinalCnt").asText())
                        .femaleClosetCnt(dataNode.path("femaleClosetCnt").asText())
                        .femaleDspsnClosetCnt(dataNode.path("femaleDspsnClosetCnt").asText())

                        .emdNm(PythonTranslator.translate(dataNode.path("emdNm").asText(), "ko", "zh-cn"))
                        .rnAdres(PythonTranslator.translate(dataNode.path("rnAdres").asText(), "ko", "zh-cn"))
                        .lnmAdres(PythonTranslator.translate(dataNode.path("lnmAdres").asText(), "ko", "zh-cn"))
                        .toiletNm(PythonTranslator.translate(dataNode.path("toiletNm").asText(), "ko", "zh-cn"))
                        .opnTimeinfo(PythonTranslator.translate(dataNode.path("opnTimeinfo").asText(), "ko", "zh-cn"))
                        .filthPprcsMthdNm(PythonTranslator.translate(dataNode.path("filthPprcsMthdNm").asText(), "ko", "zh-cn"))

                        .photo(getPhotoUrls(dataNode.path("photo")).toString())
                        .build();

                result.add(dto);
            }

            return new PageResponse<>(numOfRows, pageNo, totalCount, result);
        } catch(Exception e){
            e.printStackTrace();
        }
        return new PageResponse<>(numOfRows, pageNo, totalCount, result);
    }

    // getPhotoUrls 메서드
    private List<String> getPhotoUrls(JsonNode photoNode) {
        List<String> photoUrls = new ArrayList<>();
        if (photoNode.isArray()) {
            for (JsonNode urlNode : photoNode) {
                photoUrls.add(urlNode.asText());
            }
        }
        return photoUrls;
    }

}
