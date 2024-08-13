package com.example.jejutravel.domain.Dto.content;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusStopResponse {

    private String stationId;
    private String longitude;
    private String latitude;
    private String stationName;
    private String stationAddress;
    private String localInfo;
    private String direction;

    @Builder
    public BusStopResponse(String stationId, String longitude, String latitude,
                            String stationName, String stationAddress,String localInfo,
                            String direction){
        this.stationId = stationId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.stationName = stationName;
        this.stationAddress = stationAddress;
        this.localInfo = localInfo;
        this.direction = direction;
    }

}
