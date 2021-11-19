package com.bitravel.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reviewComment")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name="rCommentId")
	private Long rCommentId;
	
	@ManyToOne(targetEntity=Review.class, fetch=FetchType.LAZY)
	@JoinColumn(name="reviewId")
	private Review review;
	
	@Column(name="rCommentRecom")
	private Integer rCommentRecom;
	
	@Column(name="rCommentLevel")
	private Integer rCommentLevel;
	
	@Column(name = "rCommentContent")
	private String rCommentContent;
	
	@Column(name = "rCommentDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp rCommentDate;
	
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
	
	@Builder
	public ReviewComment(Long commentId, Review review, int commentRecom, int commentLevel, String commentContent, Timestamp date, String userEmail, String nickname) {
		this.rCommentId = commentId;
		this.review = review;
		this.rCommentContent = commentContent;
		this.rCommentDate = date;
		this.rCommentLevel = commentLevel;
		this.rCommentRecom = commentRecom;
		this.userEmail = userEmail;
		this.nickname = nickname;
		
	}
	
    public void update(String commentContent) {
    	this.rCommentContent = commentContent;
    }
	
}
