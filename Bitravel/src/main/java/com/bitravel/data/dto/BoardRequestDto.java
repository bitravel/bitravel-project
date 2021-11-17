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

    public Board toEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .userEmail(userEmail)
                .boardView(0)
                .build();
    }

	public void setUserEmail(String email) {
		this.userEmail = email;
	}
}
