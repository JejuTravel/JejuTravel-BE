package com.example.jejutravel.controller;

import com.example.jejutravel.global.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CalenderController {

    // 일정 생성
    @PostMapping(value = "/create/event", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse<?> createEvent(
            @RequestBody MultiValueMap<String, String> formData,
            @RequestHeader("Authorization") String authorizationHeader) {

//        String token = AccessToken;
        // Authorization 헤더에서 토큰 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.createError("카카오 로그인을 먼저 진행해주세요. AccessToken이 존재하지 않습니다.");
        }

        String token = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 추출
        System.out.println("Received Access Token: " + token);

        /*
        POST /api/v1/create/event 요청 사항
        Authorization: Bearer your-access-token-here
        Content-Type: application/x-www-form-urlencoded
        event=...
         */
        try {
            String apiUrl = "https://kapi.kakao.com/v2/api/calendar/create/event";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // Content-Type 설정

            // HttpEntity에 요청 데이터와 헤더를 포함시킵니다.
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

            // RestTemplate를 사용하여 POST 요청을 보냅니다.
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // JSON 문자열을 Map으로 변환하여 이스케이프 문자를 제거
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(response.getBody(), Map.class);

            // Map을 그대로 반환하여 JSON 형태로 직렬화되도록 설정
            return ApiResponse.createSuccessWithMessage(responseData, "톡캘린더 일정이 생성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("톡캘린더 일정 생성 중 오류가 발생했습니다.");
        }
    }
    
    // 목록 조회
    @GetMapping("/events")
    public ApiResponse<?> events(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
//        String token = AccessToken;

        // Authorization 헤더에서 토큰 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.createError("카카오 로그인을 먼저 진행해주세요. AccessToken이 존재하지 않습니다.");
        }
        String token = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 추출
        System.out.println("Received Access Token: " + token);

        try {
            String apiUrl = "https://kapi.kakao.com/v2/api/calendar/events" +
                    "?from=" + from +
                    "&to=" + to;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // JSON 문자열을 Map으로 변환하여 이스케이프 문자를 제거
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(response.getBody(), Map.class);

            // Map을 그대로 반환하여 JSON 형태로 직렬화되도록 설정
            return ApiResponse.createSuccessWithMessage(responseData, "톡캘린더 목록 조회 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("톡캘린더 목록 조회 중 오류가 발생했습니다.");
        }
    }
    
    // 상세 조회
    @GetMapping("/event")   
    public ApiResponse<?> event(
            @RequestParam("event_id") String event_id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
//        String token = AccessToken;
        // Authorization 헤더에서 토큰 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.createError("카카오 로그인을 먼저 진행해주세요. AccessToken이 존재하지 않습니다.");
        }
        String token = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 추출
        System.out.println("Received Access Token: " + token);

        try {
            String apiUrl = "https://kapi.kakao.com/v2/api/calendar/event" +
                    "?event_id=" + event_id;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // JSON 문자열을 Map으로 변환하여 이스케이프 문자를 제거
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(response.getBody(), Map.class);

            // Map을 그대로 반환하여 JSON 형태로 직렬화되도록 설정
            return ApiResponse.createSuccessWithMessage(responseData, "톡캘린더 상세 조회 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("톡캘린더 상세 조회 중 오류가 발생했습니다.");
        }
    }

    // 일정 수정
    @PostMapping(value = "/update/event", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse<?> updateEvent(
            @RequestBody MultiValueMap<String, String> paramMap,
            @RequestHeader("Authorization") String authorizationHeader) {
//        String token = AccessToken;
        // Authorization 헤더에서 토큰 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.createError("카카오 로그인을 먼저 진행해주세요. AccessToken이 존재하지 않습니다.");
        }
        String token = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 추출
        System.out.println("Received Access Token: " + token);

        // 필요한 데이터를 MultiValueMap에서 추출
        String event = paramMap.getFirst("event");
        String recurUpdateType = paramMap.getFirst("recur_update_type") ;
        String eventId = paramMap.getFirst("event_id");

        // 해당 필드들이 null이 아닌지 확인
        if (event == null || recurUpdateType == null || eventId == null) {
            return ApiResponse.createError("필수 데이터가 누락되었습니다.");
        }

        try {
            String apiUrl = "https://kapi.kakao.com/v2/api/calendar/update/event/host";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // MultiValueMap으로 데이터를 전송
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // 응답 본문이 비어있지 않은지 확인 // 반복되는 일정의 경우, 원래 응답이 비어있다.
            String responseBody = response.getBody();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                return ApiResponse.createSuccessWithMessage(null,"반복되는 톡캘린더 일정이 업데이트되었습니다.");
            }

            // 하나의 일정인 경우, (recurUpdateType = "THIS"으로 기본 값이 들어가도) event id가 도출된다.
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(responseBody, Map.class);

            return ApiResponse.createSuccessWithMessage(responseData, "톡캘린더 일정이 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("톡캘린더 일정 업데이트 중 오류가 발생했습니다.");
        }
    }

    // 일정 삭제
    @DeleteMapping( "/delete/event")
    public ApiResponse<?> deleteEvent(
            @RequestParam("event_id") String event_id,
            @RequestParam(value = "recur_update_type", defaultValue = "THIS") String recur_update_type,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

//        String token = AccessToken;
        // Authorization 헤더에서 토큰 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ApiResponse.createError("카카오 로그인을 먼저 진행해주세요. AccessToken이 존재하지 않습니다.");
        }
        String token = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 추출
        System.out.println("Received Access Token: " + token);

        try {
            // API URL 구성
            String apiUrl = "https://kapi.kakao.com/v2/api/calendar/delete/event?event_id=" +
                    event_id + "&recur_update_type=" + recur_update_type;

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // HttpEntity 생성
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // RestTemplate을 사용하여 DELETE 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.DELETE, entity, String.class);

            // 응답 본문 원래 비어있다.
            // 반복 일정의 경우, 원래 빈 응답.
            // 단일 일정의 경우, 일정 id가 원래는 응답되지만, @RequestParam(value = "recur_update_type", defaultValue = "THIS")으로 기본값이 주어지기에,
                            //  항상 빈 응답 도출 됨.
            String responseBody = response.getBody();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                return ApiResponse.createSuccessWithMessage(null,"톡캘린더 일정이 삭제었습니다.");
            }

            // 사실 상 필요 없는 코드이긴 하나, 추후 보수 및 return 값 필요
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(responseBody, Map.class);
            return ApiResponse.createSuccessWithMessage(responseData, "톡캘린더 일정이 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("톡캘린더 일정 삭제 중 오류가 발생했습니다.");
        }
    }

}

