package com.example.jejutravel.domain.Dto.content;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicWifiResponse {
    private String longitude;
    private String latitude;
    private String apGroupName;
    private String installLocationDetail;
    private String category;
    private String categoryDetail;
    private String addressDong;
    private String addressDetail;

    @Builder
    public PublicWifiResponse(String longitude, String latitude,
                              String apGroupName, String installLocationDetail,
                              String category, String categoryDetail,
                              String addressDong, String addressDetail){
        this.longitude = longitude;
        this.latitude = latitude;
        this.apGroupName = apGroupName;
        this.installLocationDetail = installLocationDetail;
        this.category = category;
        this.categoryDetail = categoryDetail;
        this.addressDong = addressDong;
        this.addressDetail = addressDetail;
    }

}
