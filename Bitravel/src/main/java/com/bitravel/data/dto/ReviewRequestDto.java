package com.bitravel.data.dto;

import java.util.List;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.Travel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReviewRequestDto {

	private String userEmail; // 작성자 (이메일)
    private String reviewTitle; // 제목
    private String reviewContent; // 내용
    private Integer reviewLevel; // 글 레벨 (답글 여부 확인)
    private List<Long> travelId; // 부여된 여행지 정보들
    
    @JsonIgnore
    private List<Travel> travelList;
    
    public Review toEntity() {
        return Review.builder()
        		.travelList(travelList)
                .reviewTitle(reviewTitle)
                .reviewContent(reviewContent)
                .userEmail(userEmail)
                .reviewLevel(reviewLevel)
                .build();
    }

	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
	public void setTravelList(List<Travel> list) {
		this.travelList = list;
	}
}
