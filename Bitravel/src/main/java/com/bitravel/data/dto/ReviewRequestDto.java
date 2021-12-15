package com.bitravel.data.dto;

import java.util.List;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewTravels;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReviewRequestDto {
	
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
    private String reviewTitle; // 제목
    private String reviewContent; // 내용
    private Integer reviewLevel; // 글 레벨 (답글 여부 확인)
    private List<Long> travelId; // 부여된 여행지 정보들
    private String thumbNail; // 썸네일 경로
    private List<String> travelName; //여행지 이름
    private List<String> latitude; // 위도
    private List<String> longitude; // 경도
    private List<String> isLiked; // 선호도
    private List<String> travelImage; //여행지 사진
    
    @JsonIgnore
    private List<ReviewTravels> reviewTravels;
    
    public Review toEntity() {
        return Review.builder()
                .reviewTitle(reviewTitle)
                .reviewContent(reviewContent)
                .userEmail(userEmail)
                .nickname(nickname)
                .thumbNail(thumbNail)
                //.reviewLevel(reviewLevel)
        		//.travelSet(travel)
                .build();
    }

	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
	public void setTravel(List<ReviewTravels> reviewTravels) {
		this.reviewTravels =reviewTravels;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
