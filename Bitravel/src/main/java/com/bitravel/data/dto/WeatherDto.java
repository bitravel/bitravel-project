package com.bitravel.data.dto;

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
public class WeatherDto {

	private String timeShort;
	private String dayShort;
	private String timeMiddle;
	private String latitude;
	private String longitude;
	private String regionId;
	
}
