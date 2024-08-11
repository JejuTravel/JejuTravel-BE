package com.example.jejutravel.service;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.jejutravel.global.api.PageResponse;
import com.example.jejutravel.domain.Dto.content.ContentListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TourismService {
	public PageResponse<ContentListResponse> tourismFetch(String apiUrl,int pageNo) {
		List<ContentListResponse> result = new ArrayList<>();
		int totalCount = 0;
		int pageSize = 10;

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);

			String responseString = restTemplate.getForObject(uri, String.class);

			// XML인지 JSON인지 확인
			// XML - 오류
			if (responseString != null && responseString.trim().startsWith("<")) {
				// XML parsing
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseString)));

				NodeList errMsgNodes = document.getElementsByTagName("errMsg");
				if (errMsgNodes.getLength() > 0) {
					String errMsg = errMsgNodes.item(0).getTextContent();
					log.error("Error Message: {}", errMsg);
				}

				NodeList returnAuthMsgNodes = document.getElementsByTagName("returnAuthMsg");
				if (returnAuthMsgNodes.getLength() > 0) {
					String returnAuthMsg = returnAuthMsgNodes.item(0).getTextContent();
					log.error("Return Auth Message: {}", returnAuthMsg);
					if ("SERVICE_KEY_IS_NOT_REGISTERED_ERROR".equals(returnAuthMsg)) {
						log.error("서비스 키가 등록되지 않았습니다.");
					}
				}

				return new PageResponse<>(pageSize, pageNo, totalCount, result);
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");

			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");

			JSONObject jsonItems = (JSONObject) jsonBody.get("items");

			JSONArray jsonItemList = (JSONArray) jsonItems.get("item");

			totalCount = ((Number) jsonBody.get("totalCount")).intValue();

			for (Object o : jsonItemList) {
				JSONObject item = (JSONObject) o;
				ContentListResponse dto = makeLocationDto(item);
				if (dto == null) continue;
				result.add(dto);
			}
			return new PageResponse<>(pageSize, pageNo, totalCount, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PageResponse<>(pageSize, pageNo, totalCount, result);
	}

	// 콘텐츠 정보 JSON 을 DTO 로 변환
	public ContentListResponse makeLocationDto(JSONObject item) {
		return ContentListResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.firstImage((String) item.get("firstimage"))
			.tel((String) item.get("tel"))
			.zipcode((String) item.get("zipcode"))
			.build();
	}

	// JSON 객체에서 Long 타입 값 가져오기
	private Long parseLong(Object value) {
		if (value instanceof Number) {
			return ((Number) value).longValue();
		} else if (value instanceof String) {
			try {
				return Long.parseLong((String) value);
			} catch (NumberFormatException e) {
				log.error("Failed to parse Long from String: {}", value, e);
			}
		}
		return null;
	}
}
