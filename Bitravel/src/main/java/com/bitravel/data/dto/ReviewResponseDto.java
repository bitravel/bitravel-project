package com.bitravel.data.dto;

import java.sql.Timestamp;
import java.util.List;

import com.bitravel.data.entity.Review;

import lombok.Getter;

@Getter

// 여행지와의 연결점은 추후 확인 예정 (단순 ID로 하는 것이 좋을지, 외래키로 사용할지)
public class ReviewResponseDto {
	
	private Long reviewId; // PK
	private String userEmail; // 작성자
    private String reviewTitle; // 제목
    private String reviewContent; // 내용
    private int reviewLevel; // 레벨
    private int reviewView; // 조회수
    private Timestamp reviewDate; // 작성날짜
    private int reviewRecom; // 추천수
    private List<Long> travelId; // 부여된 여행지 정보들

    public ReviewResponseDto(Review entity) {
    	this.reviewTitle = entity.getReviewTitle();
    	this.reviewContent = entity.getReviewContent();
    	this.reviewId = entity.getReviewId();
    	this.reviewView = entity.getReviewView();
    	this.reviewLevel = entity.getReviewLevel();
    	this.reviewRecom = entity.getReviewRecom();
    	this.reviewDate = entity.getReviewDate();
    	this.userEmail = entity.getUserEmail();
    }
}