package com.bitravel.data.dto;

import java.sql.Timestamp;
import java.util.List;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewTravels;

import lombok.Getter;

@Getter
public class ReviewResponseDto {
	
	private Long reviewId; // PK
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
    private String reviewTitle; // 제목
    private String reviewContent; // 내용
    private int reviewLevel; // 레벨
    private int reviewView; // 조회수
    private Timestamp reviewDate; // 작성날짜
    private int reviewRecom; // 추천수
    private String thumbNail; //썸네일
    //private List<TravelSimpleDto> travelList;
    //private String travelName;
    
    public ReviewResponseDto(Review entity) {
    	this.reviewTitle = entity.getReviewTitle();
    	this.reviewContent = entity.getReviewContent();
    	this.reviewId = entity.getReviewId();
    	this.reviewView = entity.getReviewView();
    	this.reviewLevel = entity.getReviewLevel();
    	this.reviewRecom = entity.getReviewRecom();
    	this.reviewDate = entity.getReviewDate();
    	this.userEmail = entity.getUserEmail();
    	this.nickname = entity.getNickname();
    	this.thumbNail = entity.getThumbNail();
    	//this.travelName = rtEntity.getTravelName();
    	//this.travelList = this.extractId(entity.getTravelSet());
    }
    
    // 리뷰에 필요한 정보만 가지고 있는 Dto로 travelSet을 만들어서 사용
//    public List<TravelSimpleDto> extractId(Set<Travel> travelSet) {
//    	List<TravelSimpleDto> list = new ArrayList<>();
//    	Iterator<Travel> iterator = travelSet.iterator();
//    	while(iterator.hasNext()) {
//    		list.add(new TravelSimpleDto(iterator.next()));
//    	}
//    	return list;
//    }
  
}