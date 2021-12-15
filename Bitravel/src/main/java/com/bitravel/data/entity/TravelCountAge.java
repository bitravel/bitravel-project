package com.bitravel.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "travelCountAge")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TravelCountAge {

	@Id
	@Column(name="age")
	private String age;
    
	@Column(name="seoul")
	private Double seoul;
	
	@Column(name="busan")
	private Double busan;
	
	@Column(name="daegu")
	private Double daegu;
	
	@Column(name="incheon")
	private Double incheon;
	
	@Column(name="gwangju")
	private Double gwangju;
	
	@Column(name="daejeon")
	private Double daejeon;
	
	@Column(name="ulsan")
	private Double ulsan;
	
	@Column(name="sejong")
	private Double sejong;
	
	@Column(name="gyeonggi")
	private Double gyeonggi;
	
	@Column(name="gangwon")
	private Double gangwon;
	
	@Column(name="chungbuk")
	private Double chungbuk;
	
	@Column(name="chungnam")
	private Double chungnam;
	
	@Column(name="jeonbuk")
	private Double jeonbuk;
	
	@Column(name="jeonnam")
	private Double jeonnam;
	
	@Column(name="gyeongbuk")
	private Double gyeongbuk;
	
	@Column(name="gyeongnam")
	private Double gyeongnam;
	
	@Column(name="jeju")
	private Double jeju;
}

