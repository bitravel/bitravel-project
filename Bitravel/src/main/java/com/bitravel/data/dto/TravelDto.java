package com.bitravel.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TravelDto {

	private Long travelId;
	private String taravelName;
	private String travelSummary;
	private String latitude;
	private String longitude;
	private String address;
	private String travelTel; 
	private String travelDetail;
	private String parking;
	private String baby;
	private String pet;
	private String card;
	private String largeGov;
	private String smallGov;
}
