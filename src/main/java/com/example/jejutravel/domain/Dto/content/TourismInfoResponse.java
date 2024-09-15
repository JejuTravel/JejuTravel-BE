package com.example.jejutravel.domain.Dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TourismInfoResponse {

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

	private String heritage1;
	private String infocenter;
	private String parking;
	private String restdate;
	private String usetime;

	@Builder
	public TourismInfoResponse(String title,String address, Long areaCode, Long contentId, Long contentTypeId, String cat3, String firstImage,String firstImage2,
		String tel, String homepage, String zipcode, String overview, String directions, Long sigunguCode, String createdtime, String modifiedtime,
		String heritage1, String infocenter, String parking, String restdate, String usetime) {
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

		this.heritage1 = heritage1;
		this.infocenter = infocenter;
		this.parking = parking;
		this.restdate = restdate;
		this.usetime = usetime;
	}

}
