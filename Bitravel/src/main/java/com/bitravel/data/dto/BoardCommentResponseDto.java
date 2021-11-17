package com.bitravel.data.dto;

import com.bitravel.data.entity.BoardComment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class BoardCommentResponseDto {
	
	private Long bCommentId;
	private String userEmail; // 작성자 (이메일)
	private String commentContent; // 내용
	private Integer commentLevel; // 댓글 레벨 (대댓글 여부 확인)
	private Long boardId;
	
	public BoardCommentResponseDto (BoardComment bComment) {
		this.bCommentId = bComment.getBCommentId();
		this.boardId = bComment.getBoard().getBoardId();
		this.commentContent = bComment.getBCommentContent();
		this.userEmail = bComment.getUserEmail();
		this.commentLevel = bComment.getBCommentLevel();
	}
	
	public void setUserEmail(String email) {
		this.userEmail = email;
	}
	
}
