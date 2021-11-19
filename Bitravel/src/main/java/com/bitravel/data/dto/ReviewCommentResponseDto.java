package com.bitravel.data.dto;

import com.bitravel.data.entity.ReviewComment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class ReviewCommentResponseDto {
	
	private Long rCommentId;
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
	private String commentContent; // 내용
	private Integer commentLevel; // 댓글 레벨 (대댓글 여부 확인)
	private Long reviewId;
	
	public ReviewCommentResponseDto (ReviewComment rComment) {
		this.rCommentId = rComment.getRCommentId();
		this.reviewId = rComment.getReview().getReviewId();
		this.commentContent = rComment.getRCommentContent();
		this.userEmail = rComment.getUserEmail();
		this.commentLevel = rComment.getRCommentLevel();
		this.nickname = rComment.getNickname();
	}
	
	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
}
