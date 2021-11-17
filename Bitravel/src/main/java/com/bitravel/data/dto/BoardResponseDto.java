package com.bitravel.data.dto;

import java.sql.Timestamp;

import com.bitravel.data.entity.Board;

import lombok.Getter;

@Getter

public class BoardResponseDto {
	
	private Long boardId; // PK
	private String userEmail; // 작성자
    private String boardTitle; // 제목
    private String boardContent; // 내용
    private int boardLevel; // 레벨
    private int boardView; // 조회수
    private Timestamp boardDate; // 작성날짜
    private int boardRecom; // 추천수

    public BoardResponseDto(Board entity) {
    	this.boardTitle = entity.getBoardTitle();
    	this.boardContent = entity.getBoardContent();
    	this.boardId = entity.getBoardId();
    	this.boardView = entity.getBoardView();
    	this.boardLevel = entity.getBoardLevel();
    	this.boardRecom = entity.getBoardRecom();
    	this.boardDate = entity.getBoardDate();
    	this.userEmail = entity.getUserEmail();
    }
}