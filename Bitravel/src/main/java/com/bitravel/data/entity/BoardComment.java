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
@Table(name="boardComment")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name="bCommentId")
	private Long bCommentId;
	
	@ManyToOne(targetEntity=Board.class, fetch=FetchType.LAZY)
	@JoinColumn(name="boardId")
	private Board board;
	
	@Column(name="bCommentRecom")
	private Integer bCommentRecom;
	
	@Column(name="bCommentLevel")
	private Integer bCommentLevel;
	
	@Column(name = "bCommentContent")
	private String bCommentContent;
	
	@Column(name = "bCommentDate", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp bCommentDate;
	
	private String userEmail; // 작성자 이메일
	private String nickname; // 작성자 닉네임
	
	@Builder
	public BoardComment(Long commentId, Board board, int commentRecom, int commentLevel, String commentContent, Timestamp date, String userEmail, String nickname) {
		this.bCommentId = commentId;
		this.board = board;
		this.bCommentContent = commentContent;
		this.bCommentDate = date;
		this.bCommentLevel = commentLevel;
		this.bCommentRecom = commentRecom;
		this.userEmail = userEmail;
		this.nickname = nickname;
	}
	
    public void update(String commentContent) {
    	this.bCommentContent = commentContent;
    }
	
}
