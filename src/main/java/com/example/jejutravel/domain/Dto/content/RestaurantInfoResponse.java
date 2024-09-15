package com.example.jejutravel.domain.Dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantInfoResponse {

	private Long contentId;
	private Long contentTypeId;
	private String cat3;
	private String title;
	private String address;
	private Long areaCode;
	private String tel;
	private String homepage;
	private String firstImage;
	private String firstImage2;
	private String zipcode;
	private String overview;
	private String directions;
	private Long sigunguCode;
	private String createdtime;
	private String modifiedtime;

	private String firstmenu;
	private String infocenterfood;
	private String opentimefood;
	private String parkingfood;
	private String reservationfood;
	private String restdatefood;
	private String seat;
	private String smoking;
	private String treatmenu;

	@Builder
	public RestaurantInfoResponse(String title,String address, Long areaCode, Long contentId, Long contentTypeId, String cat3, String firstImage,String firstImage2,
		String tel, String homepage, String zipcode, String overview, String directions, Long sigunguCode, String createdtime, String modifiedtime,
		String firstmenu, String infocenterfood, String opentimefood, String parkingfood, String reservationfood, String restdatefood, String seat,
		String smoking, String treatmenu) {
		this.title = title;
		this.address = address;
		this.areaCode = areaCode ;
		this.contentId = contentId ;
		this.contentTypeId = contentTypeId;
		this.cat3 = cat3;
		this.firstImage = firstImage;
		this.firstImage2 = firstImage2;
		this.tel = tel ;
		this.homepage = homepage;
		this.zipcode = zipcode ;
		this.overview = overview ;
		this.directions = directions ;
		this.sigunguCode = sigunguCode ;
		this.createdtime = createdtime ;
		this.modifiedtime = modifiedtime;

		this.firstmenu = firstmenu;
		this.infocenterfood = infocenterfood;
		this.opentimefood = opentimefood;
		this.parkingfood = parkingfood;
		this.reservationfood = reservationfood;
		this.restdatefood = restdatefood;
		this.smoking = smoking;
		this.treatmenu = treatmenu;
	}
}
