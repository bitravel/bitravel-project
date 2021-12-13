package com.bitravel.data.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@Table(name = "review")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
	// 초안 기준 1차 설계 진행중
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "reviewId")
    private Long reviewId;
    
    @NotBlank(message = "글의 제목을 기입해주세요.")
    @Column(name = "reviewTitle")
    private String reviewTitle;
    
    // 꼭 필요할지 확인
    // Request에 자동으로 user를 넘겨야 하는 건데...
    //@ManyToOne
    //@JoinColumn (name = "userId")
    //private User user;
    
    @Column(name = "reviewView") //조회수
    private Integer reviewView;
    @Column(name = "reviewRecom") //추천수
    private Integer reviewRecom;
    
    @Column(name = "reviewLevel")
    private Integer reviewLevel;
    // 답글/대댓글용S
    
    @NotBlank(message = "글의 내용을 기입해주세요.")
    private @Column(name = "reviewContent") String reviewContent;
    
    @Column(name = "reviewDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp reviewDate;
    
    private String userEmail; // 작성자 이메일
    
    private String nickname; // 작성자 닉네임
    
    private String thumbNail;
    
    @Builder
    public Review(String reviewTitle, String reviewContent, String userEmail,Long reviewId, int reviewView, int reviewRecom, int reviewLevel, Timestamp reviewDate, String nickname, String thumbNail) {
    	this.reviewTitle = reviewTitle;
    	this.reviewContent = reviewContent;
    	this.reviewId = reviewId;
    	this.reviewView = reviewView;
    	this.reviewLevel = reviewLevel;
    	this.reviewRecom = reviewRecom;
    	this.reviewDate = reviewDate;
    	this.userEmail = userEmail;
    	this.nickname = nickname;
    	//this.travelSet = travelSet;
    	this.thumbNail = thumbNail;
    }
    
    public void update(String reviewTitle, String reviewContent) {
    	this.reviewTitle = reviewTitle;
    	this.reviewContent = reviewContent;
    	//this.travelSet = travelSet;
    }
    
    @OneToMany(mappedBy ="review") //ReviewTravels 테이블의 review필드에 맵핑
    private List<ReviewTravels> reviewTravels = new ArrayList<>();
   
    //조회수 증가
    public void increaseView()
    {
    	this.reviewView++;
    }
  
}

