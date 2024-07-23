package com.example.jejutravel.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TourismController {

	@Value("${tourism.api.key}")
	private String apiKey;

	@GetMapping("/tourism")
	public String callApi() {
		StringBuilder result = new StringBuilder();
		// ResponseEntity<Map> resultMap = null ;
		try {
			String apiUrl = "https://apis.data.go.kr/B551011/ChsService1/areaBasedList1?" +
				"serviceKey=" + apiKey +
				"&numOfRows=10" +
				"&pageNo=1" +
				"&MobileOS=ETC" +
				"&MobileApp=JejuTravel" +
				"&areaCode=39" +
				"&_type=json";

			// System.out.println(apiUrl);

			URL url = new URL(apiUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json");

			// RestTemplate restTemplate = new RestTemplate();
			// HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
			// resultMap = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
			// System.out.println(resultMap.getBody());

			BufferedReader rd;
			if (urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
			}
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.toString();
	}


}
