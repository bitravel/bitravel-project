package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "imageId")
	@JsonIgnore
	private Long imageId;
	
	@Column(name = "travelName")
	private String travelName;
	
	@Column(name = "travelImage")
	private String travelImage;
	
    @Column(name = "isUpdated", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isUpdated; // 이미지 등록 완료 여부
	
}
