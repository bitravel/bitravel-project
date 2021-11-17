package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@Table(name = "Board")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
	// 초안 기준 1차 설계 진행중
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Column (name = "boardId") Long boardId;
    
    @NotBlank(message = "글의 제목을 기입해주세요.")
    @Size (max=30, message = "제목은 30자 이내로 기입해 주세요.")
    private @Column(name = "boardTitle") String boardTitle;
    
    // 글쓰기 창으로 진입하는 코드를 짜기 전에는 쉽게 꺼낼 수가 없다...ㅠ
    // Request에 자동으로 user를 넘겨야 하는 건데...
    //@ManyToOne
    //@JoinColumn (name = "userId")
    //private User user;
    
    @Column(name = "boardView") //조회수
    private Integer boardView = 0;
    @Column(name = "boardRecom") //추천수
    private Integer boardRecom = 0;
    
    private @Column(name = "boardLevel") Integer boardLevel = 0;
    // 글 쓸때는 레벨 0, 댓글은 1 -> 대댓 기능은 연구한 뒤 진행
    
    @NotBlank(message = "글의 내용을 기입해주세요.")
    @Size(min=10, message = "글의 내용은 최소 10자 이상이어야 합니다.")
    private @Column(name = "boardContent") String boardContent;
    
    @Column(name = "boardDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp boardDate;
    
    private String userEmail; //작성자
    @Builder
    public Board(String boardTitle, String boardContent, String userEmail,Long boardId, int boardView, int boardRecom, int boardLevel, Timestamp boardDate) {
    	this.boardTitle = boardTitle;
    	this.boardContent = boardContent;
    	this.boardId = boardId;
    	this.boardView = boardView;
    	this.boardLevel = boardLevel;
    	this.boardRecom = boardRecom;
    	this.boardDate = boardDate;
    	this.userEmail = userEmail;
    }
    
    public void update(String boardTitle, String boardContent) {
    	this.boardTitle = boardTitle;
    	this.boardContent = boardContent;
    }
  
}

