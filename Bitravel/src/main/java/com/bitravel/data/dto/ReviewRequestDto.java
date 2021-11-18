package com.bitravel.data.dto;

import com.bitravel.data.entity.Review;

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
    
    
    public Review toEntity() {
        return Review.builder()
                .reviewTitle(reviewTitle)
                .reviewContent(reviewContent)
                .userEmail(userEmail)
                .reviewLevel(reviewLevel)
                .build();
    }

	public void setUserEmail(String email) {
		this.userEmail = email;
	}
}
