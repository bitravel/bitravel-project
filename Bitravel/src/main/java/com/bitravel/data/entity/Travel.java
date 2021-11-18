package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Travel {
	
	// 유저가 편집할 수 없는 영역이므로 ResponseDto만 필요
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "travelId")
	private long travelId;
	
	@Column(name = "travelName") // 여행지 이름
	private String taravelName;
	
	@Column(name = "travelSummary") // 여행지 요약정보
	private String travelSummary;
	
	@Column(name = "latitude") // 위도 (세로방향)
	private String latitude;
	
	@Column(name = "longitude") // 경도 (가로방향)
	private String longitude;
	
	@Column(name = "address") // 주소
	private String address;
	
	@Column(name = "travelTel") // 전화번호
	private String travelTel; 
	
	@Column(name = "travelDetail") // 상세정보
	private String travelDetail;
	
	@Column(name = "parking") // 주차정보
	private String parking;
	
	@Column(name = "baby") // 유모차 동반
	private String baby;
	
	@Column(name = "pet") // 애완동물 동반
	private String pet;
	
	@Column(name = "card") // 신용카드 사용가능여부
	private String card;
	
	@Column(name = "largeGov") // 광역자치단체
	private String largeGov;
	
	@Column(name = "smallGov") // 기초자치단체
	private String smallGov;

}