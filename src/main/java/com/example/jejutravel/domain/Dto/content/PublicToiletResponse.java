package com.example.jejutravel.domain.Dto.content;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicToiletResponse {
    private String laCrdnt; // 위도 좌표
    private String loCrdnt; // 경도 좌표
    private String telno; // 전화번호
    private String emdNm; // 읍면동 명
    private String rnAdres; // 도로명 주소
    private String lnmAdres; // 지번 주소
    private String toiletNm; // 화장실 명
    private String opnTimeinfo; // 개방 시간 정보 ex)연중무휴
    private String filthPprcsMthdNm; // 오물 처리 방식 명 ex)수세식
    private String toiletEntrncCctvInstlYn; // 화장실 입구 CCTV 설치 여부
    private String diaperExhgTablYn; // 기저귀 교환 탁자 여부
    private String maleClosetCnt; // 남성 대변기 수
    private String maleUrinalCnt; // 남성 소변기 수
    private String maleDspsnClosetCnt; // 남성 장애인 대변기 수
    private String maleDspsnUrinalCnt; // 남성 장애인 소변기 수
//    private String maleChildClosetCnt; // 남성 어린이 대변기 수
//    private String maleChildUrinalCnt; // 남성 어린이 소변기 수
    private String femaleClosetCnt; // 여성 대변기 수
    private String femaleDspsnClosetCnt; // 여성 장애인 대변기 수
//    private String femaleChildClosetCnt; // 여성 어린이 대변기 수
//    private String etcCn; // 기타 내용
    private String photo; // 사진 URL 리스트

    @Builder
    public  PublicToiletResponse(String laCrdnt, String loCrdnt, String telno,
                                 String emdNm, String rnAdres, String lnmAdres,
                                 String toiletNm, String opnTimeinfo, String filthPprcsMthdNm,
                                 String toiletEntrncCctvInstlYn, String diaperExhgTablYn,
                                 String maleClosetCnt, String maleUrinalCnt,
                                 String maleDspsnClosetCnt, String maleDspsnUrinalCnt,
                                 String femaleClosetCnt, String femaleDspsnClosetCnt,
                                 String photo){
        this.laCrdnt = laCrdnt;
        this.loCrdnt = loCrdnt;
        this.telno = telno;
        this.emdNm = emdNm;
        this.rnAdres = rnAdres;
        this.lnmAdres = lnmAdres;
        this.toiletNm = toiletNm;
        this.opnTimeinfo =opnTimeinfo;
        this.filthPprcsMthdNm = filthPprcsMthdNm;
        this.toiletEntrncCctvInstlYn = toiletEntrncCctvInstlYn;
        this.diaperExhgTablYn = diaperExhgTablYn;
        this.maleClosetCnt = maleClosetCnt;
        this.maleUrinalCnt = maleUrinalCnt;
        this.maleDspsnClosetCnt = maleDspsnClosetCnt;
        this.maleDspsnUrinalCnt = maleDspsnUrinalCnt;
        this.femaleClosetCnt = femaleClosetCnt;
        this.femaleDspsnClosetCnt = femaleDspsnClosetCnt;
        this.photo = photo;
    }
}
