package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nameSearchRanking")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameSearchRanking {
	
	// 유저가 편집할 수 없는 영역이므로 ResponseDto만 필요
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "nsrId")
	private Long nsrId;
	
	@Column(name = "travelName") // 여행지 이름
	private String travelName;
	
	@Column(name = "ranking") // 카테고리별 검색 순위
	private Integer ranking;
	
	@Column(name = "address") // 주소
	private String address;
	
	@Column(name = "middleCategory") // 여행지 중분류
	private String middleCategory;
	
	@Column(name = "smallCategory") // 여행지 소분류
	private String smallCategory;
	
}
