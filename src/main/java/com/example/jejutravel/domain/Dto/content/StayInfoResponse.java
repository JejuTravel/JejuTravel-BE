package com.example.jejutravel.domain.Dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StayInfoResponse {
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

	private String accomcountlodging;
	private String benikia;
	private String goodstay;
	private String hanok;
	private String checkintime;
	private String checkouttime;
	private String foodplace;
	private String infocenterlodging;
	private String parkinglodging;
	private String pickup;
	private String roomcount;
	private String reservationlodging;
	private String reservationurl;
	private String roomtype;
	private String scalelodging;
	private String subfacility;

	@Builder
	public StayInfoResponse(String title,String address, Long areaCode, Long contentId, Long contentTypeId, String cat3, String firstImage,String firstImage2,
		String tel, String homepage, String zipcode, String overview, String directions, Long sigunguCode, String createdtime, String modifiedtime,
		String accomcountlodging, String benikia, String goodstay, String hanok, String checkintime, String checkouttime, String foodplace,
		String infocenterlodging, String parkinglodging, String pickup, String roomcount, String reservationlodging, String reservationurl,
		String roomtype, String scalelodging, String subfacility) {
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

		this.accomcountlodging = accomcountlodging;
		this.benikia = benikia;
		this.goodstay = goodstay;
		this.hanok = hanok;
		this.checkintime = checkintime;
		this.checkouttime = checkouttime;
		this.foodplace = foodplace;
		this.infocenterlodging = infocenterlodging;
		this.parkinglodging = parkinglodging;
		this.pickup = pickup;
		this.roomcount = roomcount;
		this.reservationlodging = reservationlodging;
		this.reservationurl = reservationurl;
		this.roomtype = roomtype;
		this.scalelodging = scalelodging;
		this.subfacility = subfacility;
	}
}
