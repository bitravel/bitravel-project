package com.bitravel.data.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
 
@Entity
@Table(name = "Board")
@Setter
@Getter
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
    
    @Column(name = "boardView")
    private Integer boardView = 0;
    @Column(name = "boardRecom")
    private Integer boardRecom = 0;
    
    private @Column(name = "boardLevel") Integer boardLevel = 0;
    // 글 쓸때는 레벨 0, 댓글은 1 -> 대댓 기능은 연구한 뒤 진행
    
    @NotBlank(message = "글의 내용을 기입해주세요.")
    @Size(min=10, message = "글의 내용은 최소 10자 이상이어야 합니다.")
    private @Column(name = "boardContent") String boardContent;
    
    @Column(name = "boardDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp boardDate;
     
  
}

