package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userTravel")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravel {
	 
	// 유저별 관심 지역을 저장하는 Entity

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userRegionId")
	private Long userTravelId;
	
	@Column(name = "userEmail")
	private String userEmail;
	
	@Column(name = "travelId")
	private Long travelId;
	
	// 가입 시에는 방문여부는 다 FALSE, 선호 여부는 선택에 따라 다르게
    @Column(name = "isVisited", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isVisited; // 방문 여부
    
    @Column(name = "isLiked", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isLiked; // 선호 여부
    
    @Column(name = "isWishlisted", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isWishlisted; // 여행지 찜 여부
	
}
