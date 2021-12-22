package com.bitravel.data.dto;

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
public class PasswordDto {
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String oldPassword;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String newPassword;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String confirmPassword;
	
}
