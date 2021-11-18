package com.bitravel.data.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.Travel;

import lombok.Getter;

@Getter
public class ReviewResponseDto {
	
	private Long reviewId; // PK
	private String userEmail; // 작성자
    private String reviewTitle; // 제목
    private String reviewContent; // 내용
    private int reviewLevel; // 레벨
    private int reviewView; // 조회수
    private Timestamp reviewDate; // 작성날짜
    private int reviewRecom; // 추천수
    private List<TravelReviewDto> travelList;

    public ReviewResponseDto(Review entity) {
    	this.reviewTitle = entity.getReviewTitle();
    	this.reviewContent = entity.getReviewContent();
    	this.reviewId = entity.getReviewId();
    	this.reviewView = entity.getReviewView();
    	this.reviewLevel = entity.getReviewLevel();
    	this.reviewRecom = entity.getReviewRecom();
    	this.reviewDate = entity.getReviewDate();
    	this.userEmail = entity.getUserEmail();
    	this.travelList = this.extractId(entity.getTravelSet());
    }
    
    public List<TravelReviewDto> extractId(Set<Travel> travelSet) {
    	List<TravelReviewDto> list = new ArrayList<>();
    	Iterator<Travel> iterator = travelSet.iterator();
    	while(iterator.hasNext()) {
    		list.add(new TravelReviewDto(iterator.next()));
    	}
    	return list;
    }
  
}