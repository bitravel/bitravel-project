package com.bitravel.data.dto;

import com.bitravel.data.entity.Region;

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
public class RegionDto {
	
	private String largeGov;
	private String smallGov;
	private String regionLat;
	private String regionLong;
	
	public RegionDto(Region entity) {
		this.largeGov = entity.getLargeGov();
		this.smallGov = entity.getSmallGov();
		this.regionLat = entity.getRegionLat();
		this.regionLong = entity.getRegionLong();
	}
}
