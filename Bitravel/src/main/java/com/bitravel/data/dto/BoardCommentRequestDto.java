package com.bitravel.data.dto;

import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.BoardComment;
import com.bitravel.util.TagUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class BoardCommentRequestDto {
	
	private Long boardId; // 원글 번호
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
	private String commentContent; // 내용
	private Integer commentLevel; // 댓글 레벨 (대댓글 여부 확인)
	
    @JsonIgnore
    private Board board;
	
	public BoardComment toEntity() {
		return BoardComment.builder()
				.board(board)
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
