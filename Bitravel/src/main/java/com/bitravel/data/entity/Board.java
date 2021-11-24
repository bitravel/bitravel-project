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
@Table(name = "board")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
	// 초안 기준 1차 설계 진행중
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "boardId")
    private Long boardId;
    
    @NotBlank(message = "글의 제목을 기입해주세요.")
    @Column(name = "boardTitle")
    private String boardTitle;
    
    // 꼭 필요할지 확인
    // Request에 자동으로 user를 넘겨야 하는 건데...
    //@ManyToOne
    //@JoinColumn (name = "userId")
    //private User user;
    
    @Column(name = "boardView") //조회수
    private Integer boardView;
    @Column(name = "boardRecom") //추천수
    private Integer boardRecom;
    
    @Column(name = "boardLevel")
    private Integer boardLevel;
    // 답글/대댓글용
    
    @Column(name = "boardFile")
    private String boardFile;
    
    @NotBlank(message = "글의 내용을 기입해주세요.")
    @Size(max=10000)
    private @Column(name = "boardContent") String boardContent;
    
    @Column(name = "boardDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp boardDate;
    
    private String userEmail; // 작성자 이메일
    private String nickname; // 작성자 닉네임
    
    @Builder
    public Board(String boardTitle, String boardContent, String userEmail,Long boardId, int boardView, int boardRecom, int boardLevel, Timestamp boardDate, String nickname, String boardFile) {
    	this.boardTitle = boardTitle;
    	this.boardContent = boardContent;
    	this.boardId = boardId;
    	this.boardView = boardView;
    	this.boardLevel = boardLevel;
    	this.boardRecom = boardRecom;
    	this.boardDate = boardDate;
    	this.userEmail = userEmail;
    	this.nickname = nickname;
    	this.boardFile = boardFile;
    }
    
    public void update(String boardTitle, String boardContent) {
    	this.boardTitle = boardTitle;
    	this.boardContent = boardContent;
    }
    
    //조회수 증가
    public void increaseVeiw()
    {
    	this.boardView++;
    }
}

