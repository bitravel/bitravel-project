package com.bitravel.data.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bitravel.data.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UserDto {
	
	@NotNull
	@Size(min=5, max=50)
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
    @Size(min=4, max=100)
	private String password;
	
	@NotNull
	@Size(min=2, max=20)
	private String nickname;
	
	@NotNull
	@Size(min=2, max=20)
	private String realname;
	
	@NotNull
	private Integer point;
	
	@NotNull
	private String gender;
	
	@NotNull
	private Integer age;
	
	public UserDto(User entity) {
		this.email = entity.getEmail();
		this.password = "unknown";
		this.nickname = entity.getNickname();
		this.realname = entity.getRealName();
		this.point = entity.getPoint();
		this.gender = entity.getGender();
		this.age = entity.getAge();
	}
	
	
	
}
