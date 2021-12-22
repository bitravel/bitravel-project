package com.bitravel.data.dto;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewComment;
import com.bitravel.util.TagUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class ReviewCommentRequestDto {
	
	private Long reviewId; // 원글 번호
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
	private String commentContent; // 내용
	private Integer commentLevel; // 댓글 레벨 (대댓글 여부 확인)
	
    @JsonIgnore
    private Review review;
	
	public ReviewComment toEntity() {
		return ReviewComment.builder()
				.review(review)
				.commentContent(TagUtil.getText(commentContent))
				.userEmail(userEmail)
				.nickname(nickname)
				//.commentLevel(commentLevel)
				.build();
	}
	
	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
