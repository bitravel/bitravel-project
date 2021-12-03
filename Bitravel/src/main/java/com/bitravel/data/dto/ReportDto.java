package com.bitravel.data.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ReportDto {
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long reportId;
	
	private String reportTitle;
	private String reportContent;
	private String reporterEmail;
	private String reportedEmail;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Timestamp reportDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Timestamp checkDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String checkResult;
}
