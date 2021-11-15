package com.bitravel.data.dto;

import java.sql.Timestamp;

import com.bitravel.data.entity.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class BoardRequestDto {

	private String userId; // 작성자
    private String boardTitle; // 제목
    private String boardContent; // 내용

    public Board toEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .userId(userId)
                .boardView(0)
                .build();
    }
}
