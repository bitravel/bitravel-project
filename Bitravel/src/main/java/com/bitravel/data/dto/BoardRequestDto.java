package com.bitravel.data.dto;

import com.bitravel.data.entity.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class BoardRequestDto {

	private String userEmail; // 작성자 (이메일)
    private String boardTitle; // 제목
    private String boardContent; // 내용
    private Integer boardLevel; // 글 레벨 (답글 여부 확인)
    
    
    public Board toEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .userEmail(userEmail)
                .boardLevel(boardLevel)
                .build();
    }

	public void setUserEmail(String email) {
		this.userEmail = email;
	}
}
