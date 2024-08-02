package main.java.com.example.jejutravel.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
// import java.io.IOException;

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

public class BusStopController {
    
    @Value("${jeju.api.key}")
	private String jejuApiKey;

    @GetMapping("/busStop")
    public String callApi(){
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
        }catch (Exception e) {
			e.printStackTrace();
		}

		return result.toString();
    }
}
