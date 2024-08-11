package com.example.jejutravel.domain.Dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StayListResponse {

	private String title;
	private String address;
	private Long areaCode;
	private Long contentId;
	private Long contentTypeId;
	private String firstImage;
	private String tel;
	private String zipcode;
	private String benikia;
	private String goodstay;
	private String hanok;
	private Long sigunguCode;

	@Builder
	public StayListResponse(String title,String address, Long areaCode, Long contentId, Long contentTypeId, String firstImage,
		String tel, String zipcode, String benikia, String goodstay, String hanok, Long sigunguCode) {
		this.title = title;
		this.address = address;
		this.areaCode = areaCode ;
		this.contentId = contentId ;
		this.contentTypeId = contentTypeId;
		this.firstImage = firstImage;
		this.tel = tel ;
		this.zipcode = zipcode ;
		this.benikia = benikia ;
		this.goodstay = goodstay ;
		this.hanok = hanok ;
		this.sigunguCode = sigunguCode ;
	}
}
