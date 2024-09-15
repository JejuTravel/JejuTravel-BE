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

import com.example.jejutravel.domain.Dto.AreaResponse;
import com.example.jejutravel.global.api.PageResponse;
import com.example.jejutravel.domain.Dto.content.RestaurantInfoResponse;
import com.example.jejutravel.domain.Dto.content.ShoppingInfoResponse;
import com.example.jejutravel.domain.Dto.content.StayInfoResponse;
import com.example.jejutravel.domain.Dto.content.StayListResponse;
import com.example.jejutravel.domain.Dto.content.TourismInfoResponse;
import com.example.jejutravel.domain.Dto.content.ContentListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenApiManager {
	/**
	 * 오픈 API 서버에서 데이터를 가져와서 알맞은 데이터 포맷으로 파싱하는 역할만 수행
	 */

	public PageResponse<ContentListResponse> fetchContentList(String apiUrl,int pageNo) {
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
			Object jsonItems = jsonBody.get("items");

			// 검색 결과 없을 경우
			if (jsonItems instanceof String && ((String) jsonItems).isEmpty()) {
				return new PageResponse<>(pageSize, pageNo, totalCount, result);
			}

			if (!(jsonItems instanceof JSONObject)) {
				return new PageResponse<>(pageSize, pageNo, totalCount, result);
			}

			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			totalCount = ((Number) jsonBody.get("totalCount")).intValue();

			for (Object o : jsonItemList) {
				JSONObject item = (JSONObject) o;
				ContentListResponse dto = makeContentDto(item);
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
	public ContentListResponse makeContentDto(JSONObject item) {
		return ContentListResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.firstImage((String) item.get("firstimage"))
			.tel((String) item.get("tel"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
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

	public PageResponse<StayListResponse> fetchStayList(String apiUrl,int pageNo) {
		List<StayListResponse> result = new ArrayList<>();
		int totalCount = 0;
		int pageSize = 10;

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);

			String responseString = restTemplate.getForObject(uri, String.class);

			// XML인지 JSON인지 확인
			// XML - 오류
			if (responseString != null && responseString.trim().startsWith("<")) {
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
			Object jsonItems = jsonBody.get("items");

			// 검색 결과 없을 경우
			if (jsonItems instanceof String && ((String) jsonItems).isEmpty()) {
				return new PageResponse<>(pageSize, pageNo, totalCount, result);
			}

			if (!(jsonItems instanceof JSONObject)) {
				return new PageResponse<>(pageSize, pageNo, totalCount, result);
			}

			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			totalCount = ((Number) jsonBody.get("totalCount")).intValue();

			for (Object o : jsonItemList) {
				JSONObject item = (JSONObject) o;
				StayListResponse dto = makeStayDto(item);
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
	public StayListResponse makeStayDto(JSONObject item) {
		return StayListResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.firstImage((String) item.get("firstimage"))
			.tel((String) item.get("tel"))
			.benikia((String) item.get("benikia"))
			.goodstay((String) item.get("goodstay"))
			.hanok((String) item.get("hanok"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
			.build();
	}

	public List<AreaResponse> getAreaCode(String apiUrl) {
		List<AreaResponse> result = new ArrayList<>();

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

				return result;
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
			Object jsonItems = jsonBody.get("items");
			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			for (Object o : jsonItemList) {
				JSONObject item = (JSONObject) o;
				AreaResponse dto = makeAreaDto(item);
				if (dto == null) continue;
				result.add(dto);
			}
			return result ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}

	public AreaResponse makeAreaDto(JSONObject item) {
		AreaResponse response = new AreaResponse(parseLong((item.get("code"))),(String) item.get("title") );
		return response;
	}

	public List<TourismInfoResponse> getTourismInfo(String apiUrl, String apiUrl2) {
		List<TourismInfoResponse> result = new ArrayList<>();

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);
			URI uri2 = new URI(apiUrl2);

			String responseString = restTemplate.getForObject(uri, String.class);
			String responseString2 = restTemplate.getForObject(uri2, String.class);

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

				return result;
			}

			if (responseString2 != null && responseString2.trim().startsWith("<")) {
				// XML parsing
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseString2)));

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

				return result;
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
			Object jsonItems = jsonBody.get("items");
			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			jsonObject = (JSONObject) jsonParser.parse(responseString2);
			jsonResponse = (JSONObject) jsonObject.get("response");
			jsonBody = (JSONObject) jsonResponse.get("body");
			jsonItems = jsonBody.get("items");
			JSONArray jsonItemList2 = (JSONArray) ((JSONObject) jsonItems).get("item");

			TourismInfoResponse dto = makeTourismInfoDto((JSONObject)jsonItemList.get(0), (JSONObject)jsonItemList2.get(0));
			result.add(dto);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public TourismInfoResponse makeTourismInfoDto(JSONObject item, JSONObject item2) {
		return TourismInfoResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.homepage((String) item.get("homepage"))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.cat3((String) item.get("cat3"))
			.firstImage((String) item.get("firstimage"))
			.firstImage2((String) item.get("firstimage2"))
			.tel((String) item.get("tel"))
			.overview((String) item.get("overview"))
			.directions((String) item.get("directions"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
			.createdtime((String) item.get("createdtime"))
			.modifiedtime((String) item.get("modifiedtime"))
			.heritage1((String) item2.get("heritage1"))
			.infocenter((String) item2.get("infocenter"))
			.parking((String) item2.get("parking"))
			.restdate((String) item2.get("restdate"))
			.usetime((String) item2.get("usetime"))
			.build();
	}

	public List<ShoppingInfoResponse> getShoppingInfo(String apiUrl, String apiUrl2) {
		List<ShoppingInfoResponse> result = new ArrayList<>();

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);
			URI uri2 = new URI(apiUrl2);

			String responseString = restTemplate.getForObject(uri, String.class);
			String responseString2 = restTemplate.getForObject(uri2, String.class);

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

				return result;
			}

			if (responseString2 != null && responseString2.trim().startsWith("<")) {
				// XML parsing
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseString2)));

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

				return result;
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
			Object jsonItems = jsonBody.get("items");
			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			jsonObject = (JSONObject) jsonParser.parse(responseString2);
			jsonResponse = (JSONObject) jsonObject.get("response");
			jsonBody = (JSONObject) jsonResponse.get("body");
			jsonItems = jsonBody.get("items");
			JSONArray jsonItemList2 = (JSONArray) ((JSONObject) jsonItems).get("item");

			ShoppingInfoResponse dto = makeShoppingInfoDto((JSONObject)jsonItemList.get(0), (JSONObject)jsonItemList2.get(0));
			result.add(dto);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public ShoppingInfoResponse makeShoppingInfoDto(JSONObject item, JSONObject item2) {
		return ShoppingInfoResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.homepage((String) item.get("homepage"))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.cat3((String) item.get("cat3"))
			.firstImage((String) item.get("firstimage"))
			.firstImage2((String) item.get("firstimage2"))
			.tel((String) item.get("tel"))
			.overview((String) item.get("overview"))
			.directions((String) item.get("directions"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
			.createdtime((String) item.get("createdtime"))
			.modifiedtime((String) item.get("modifiedtime"))
			.infocentershopping((String) item2.get("infocentershopping"))
			.opentime((String) item2.get("opentime"))
			.parkingshopping((String) item2.get("parkingshopping"))
			.restdateshopping((String) item2.get("restdateshopping"))
			.saleitem((String) item2.get("saleitem"))
			.shopguide((String) item2.get("shopguide"))
			.build();
	}

	public List<StayInfoResponse> getStayInfo(String apiUrl, String apiUrl2) {
		List<StayInfoResponse> result = new ArrayList<>();

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);
			URI uri2 = new URI(apiUrl2);

			String responseString = restTemplate.getForObject(uri, String.class);
			String responseString2 = restTemplate.getForObject(uri2, String.class);

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

				return result;
			}

			if (responseString2 != null && responseString2.trim().startsWith("<")) {
				// XML parsing
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseString2)));

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

				return result;
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
			Object jsonItems = jsonBody.get("items");
			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			jsonObject = (JSONObject) jsonParser.parse(responseString2);
			jsonResponse = (JSONObject) jsonObject.get("response");
			jsonBody = (JSONObject) jsonResponse.get("body");
			jsonItems = jsonBody.get("items");
			JSONArray jsonItemList2 = (JSONArray) ((JSONObject) jsonItems).get("item");

			StayInfoResponse dto = makeStayInfoDto((JSONObject)jsonItemList.get(0), (JSONObject)jsonItemList2.get(0));
			result.add(dto);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public StayInfoResponse makeStayInfoDto(JSONObject item, JSONObject item2) {
		return StayInfoResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.homepage((String) item.get("homepage"))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.cat3((String) item.get("cat3"))
			.firstImage((String) item.get("firstimage"))
			.firstImage2((String) item.get("firstimage2"))
			.tel((String) item.get("tel"))
			.overview((String) item.get("overview"))
			.directions((String) item.get("directions"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
			.createdtime((String) item.get("createdtime"))
			.modifiedtime((String) item.get("modifiedtime"))
			.accomcountlodging((String) item2.get("accomcountlodging"))
			.benikia((String) item2.get("opentime"))
			.goodstay((String) item2.get("goodstay"))
			.hanok((String) item2.get("hanok"))
			.checkintime((String) item2.get("checkintime"))
			.checkouttime((String) item2.get("checkouttime"))
			.foodplace((String) item.get("foodplace"))
			.infocenterlodging((String) item2.get("infocenterlodging"))
			.parkinglodging((String) item2.get("parkinglodging"))
			.pickup((String) item2.get("pickup"))
			.roomcount((String) item2.get("roomcount"))
			.reservationlodging((String) item2.get("reservationlodging"))
			.reservationurl((String) item2.get("reservationurl"))
			.roomtype((String) item2.get("roomtype"))
			.scalelodging((String) item2.get("scalelodging"))
			.subfacility((String) item2.get("subfacility"))
			.build();
	}

	public List<RestaurantInfoResponse> getRestaurantInfo(String apiUrl, String apiUrl2) {
		List<RestaurantInfoResponse> result = new ArrayList<>();

		try {
			RestTemplate restTemplate = new RestTemplate();
			URI uri = new URI(apiUrl);
			URI uri2 = new URI(apiUrl2);

			String responseString = restTemplate.getForObject(uri, String.class);
			String responseString2 = restTemplate.getForObject(uri2, String.class);

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

				return result;
			}

			if (responseString2 != null && responseString2.trim().startsWith("<")) {
				// XML parsing
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(responseString2)));

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

				return result;
			}

			// JSON - 성공
			// JSON parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
			JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
			JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
			Object jsonItems = jsonBody.get("items");
			JSONArray jsonItemList = (JSONArray) ((JSONObject) jsonItems).get("item");

			jsonObject = (JSONObject) jsonParser.parse(responseString2);
			jsonResponse = (JSONObject) jsonObject.get("response");
			jsonBody = (JSONObject) jsonResponse.get("body");
			jsonItems = jsonBody.get("items");
			JSONArray jsonItemList2 = (JSONArray) ((JSONObject) jsonItems).get("item");

			RestaurantInfoResponse dto = makeRestaurantInfoDto((JSONObject)jsonItemList.get(0), (JSONObject)jsonItemList2.get(0));
			result.add(dto);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public RestaurantInfoResponse makeRestaurantInfoDto(JSONObject item, JSONObject item2) {
		return RestaurantInfoResponse.builder()
			.title((String) item.get("title"))
			.address((String) item.get("addr1"))
			.areaCode(parseLong(item.get("areacode")))
			.homepage((String) item.get("homepage"))
			.contentId(parseLong((item.get("contentid"))))
			.contentTypeId(parseLong(item.get("contenttypeid")))
			.cat3((String) item.get("cat3"))
			.firstImage((String) item.get("firstimage"))
			.firstImage2((String) item.get("firstimage2"))
			.tel((String) item.get("tel"))
			.overview((String) item.get("overview"))
			.directions((String) item.get("directions"))
			.sigunguCode(parseLong((item.get("sigungucode"))))
			.createdtime((String) item.get("createdtime"))
			.modifiedtime((String) item.get("modifiedtime"))
			.firstmenu((String) item2.get("firstmenu"))
			.infocenterfood((String) item2.get("infocenterfood"))
			.opentimefood((String) item2.get("opentimefood"))
			.parkingfood((String) item2.get("parkingfood"))
			.reservationfood((String) item2.get("reservationfood"))
			.restdatefood((String) item2.get("restdatefood"))
			.seat((String) item.get("seat"))
			.smoking((String) item2.get("smoking"))
			.treatmenu((String) item2.get("treatmenu"))
			.build();
	}
}
