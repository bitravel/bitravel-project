package com.bitravel.data.dto;

import com.bitravel.data.entity.Travel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelSimpleDto {

	private Long travelId;
	private String travelName;
	private String latitude;
	private String longitude;
	private String address;
	private String largeGov;
	private String smallGov;
	private Integer travelView;
	
	public TravelSimpleDto(Travel entity) {
		this.travelId = entity.getTravelId();
		this.travelName = entity.getTravelName();
		this.latitude = entity.getLatitude();
		this.longitude = entity.getLongitude();
		this.address = entity.getAddress();
		this.largeGov = entity.getLargeGov();
		this.smallGov = entity.getSmallGov();
		this.travelView = entity.getTravelView();
	}
	
}
