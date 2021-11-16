package com.bitravel.data.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	@NotNull
	@Size(min=1, max=50)
	private String email;
	
	@NotNull
    @Size(min=1, max=100)
	private String password;

}
