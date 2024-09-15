package com.example.jejutravel.domain.Dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShoppingInfoResponse {

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

	private String infocentershopping;
	private String opentime;
	private String parkingshopping;
	private String restdateshopping;
	private String saleitem;
	private String shopguide;

	@Builder
	public ShoppingInfoResponse(String title,String address, Long areaCode, Long contentId, Long contentTypeId, String cat3, String firstImage,String firstImage2,
		String tel, String homepage, String zipcode, String overview, String directions, Long sigunguCode, String createdtime, String modifiedtime,
		String infocentershopping, String opentime, String parkingshopping, String restdateshopping, String saleitem, String shopguide) {
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

		this.infocentershopping = infocentershopping;
		this.opentime = opentime;
		this.parkingshopping = parkingshopping;
		this.restdateshopping = restdateshopping;
		this.saleitem = saleitem;
		this.shopguide = shopguide;
	}
}
