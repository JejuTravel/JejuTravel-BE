package com.example.jejutravel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.jejutravel.global.api.ApiResponse;
import com.example.jejutravel.service.kakaoService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class kakaoController {

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    // 사용자가 kakao 로그인은 진행하면, 결과 'code'를 redirect url로 전송해준다.
    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

//     카카오 계정과 함께 로그아웃 할 경우
//    application.properties에 값 넣어주기 -> kakao.logout.redirect_uri=http://localhost:5173/
//    @Value("${kakao.logout.redirect_uri}")
//    private String kakaoLogoutRedirectUri;

    private final kakaoService kakaoService;

    // 로그인 버튼(/login) 클릭 시, 로그인하는 url로 이동
    @GetMapping(value = "/login")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=").append(kakaoApiKey);
        url.append("&redirect_uri=").append(kakaoRedirectUri);
        url.append("&response_type=code");
        url.append("&scope=talk_calendar");

        return "redirect:" + url.toString(); // redirect를 반환
    }

    @ResponseBody
    @GetMapping("/kakao") // redirect url 주소. 사용자가 로그인 한 결과 'String code'를 받아온다.
    public  ApiResponse<?> kakaoLogin(@RequestParam(required = false) String code) {

        try {
            // URL에 포함된 code를 이용하여 액세스 토큰 발급
            String accessToken = kakaoService.getKakaoAccessToken(code);
//            System.out.println("로그인 한 후, code를 통해 얻은 access Token"+accessToken);

//            // 액세스 토큰을 이용하여 카카오 서버에서 유저 정보(닉네임, 이메일) 받아오기
//            HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
//            System.out.println("login Controller : " + userInfo);

            return ApiResponse.createSuccessWithMessage(accessToken, "카카오 로그인 Access Token이 발급되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("카카오 로그인 중 오류가 발생했습니다.");
        }
    }

    // 로그아웃
    // 공식 문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#logout
    @GetMapping("/logout")
    public ApiResponse<?> kakaoLogout(
            @RequestHeader("Authorization") String accessToken
    ) {
        try {
            // 카카오 로그아웃 API 호출
            RestTemplate restTemplate = new RestTemplate();
            String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ApiResponse.createSuccessWithMessage(accessToken, "카카오 로그아웃 성공하였습니다.");
            } else {
                return ApiResponse.createError("카카오 로그아웃 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("카카오 로그아웃 중 오류가 발생했습니다.");
        }
    }
//    // 카카오 계정과 함께 로그아웃
//    @GetMapping("/logout")
//    public ApiResponse<?> kakaoLogout() {
//        try {
//            // 로그아웃을 위한 URL 생성
//            StringBuffer url = new StringBuffer();
//            url.append("https://kapi.kakao.com/v1/user/logout?");
//            url.append("client_id=").append(kakaoApiKey);
//            url.append("&logout_redirect_uri=").append(kakaoLogoutRedirectUri);
//
//            // 클라이언트가 해당 URL로 리다이렉트되도록 처리
//            HttpHeaders headers = new HttpHeaders();
//            headers.setLocation(new URI(url.toString()));
//
//            return ApiResponse.createSuccessWithMessage(headers, "카카오 로그아웃 되었습니다.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiResponse.createError("카카오 로그아웃 중 오류가 발생했습니다.");
//        }
//    }

    // wechat 정식 등록 후 사용 가능
    // 로그인 버튼(/wechat/code) 클릭 시, 로그인하는 url로 이동
    // https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE
    @GetMapping(value = "/wechat/code")
    public String wechatConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://open.weixin.qq.com/connect/qrconnect?");
//        url.append("appid=APPID").append(wechatApiKey);
//        url.append("&redirect_uri=").append(wechatRedirectUri);
        url.append("&response_type=code");
        url.append("&scope=snsapi_login");
//        url.append("&state").append(state);
        return "redirect:" + url.toString(); // redirect를 반환
    }

    // wechat 정식 등록 후 사용 가능
    @GetMapping("/wechat") // redirect url 주소. 사용자가 로그인 한 결과 'String code'를 받아온다.
    public ApiResponse<?> wechatLogin(@RequestParam(required = false) String code) {
        try {
            // URL에 포함된 code를 이용하여 액세스 토큰 발급
            String accessToken = kakaoService.getKakaoAccessToken(code); // wechat만의 getWechatAccessToken 메소드 정의해주자.
            System.out.println("로그인 한 후, code를 통해 얻은 access Token"+accessToken);
            return ApiResponse.createSuccessWithMessage(accessToken, "wechat 로그인 Access Token이 발급되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.createError("wechat 로그인 중 오류가 발생했습니다.");
        }
    }
}
