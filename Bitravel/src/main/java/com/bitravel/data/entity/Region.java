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
@Table(name="region")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "regionId", length=50)
	@JsonIgnore
	private Long regionId;
	
	@Column(name = "largeGov")
	private String largeGov;
	
	@Column(name = "smallGov")
	private String smallGov;
	
	@Column(name = "regionLat")
	private String regionLat; // 해당 지역 latitude
	
	@Column(name = "regionLong")
	private String regionLong; // 해당 지역 Longitude
	
}
