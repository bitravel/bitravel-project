package com.bitravel.data.dto;

import javax.validation.constraints.NotNull;

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
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long userId;
	
	@NotNull
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	private String password; // 회원가입 시에만 사용함. 그 외에는 외부로 유출되지 않음
	
	@NotNull
	private String nickname;
	
	@NotNull
	private String realname;
	
	private Integer point;
	
	@NotNull
	private String gender;
	
	@NotNull
	private String ageString;
	
	// 나이 입력은 String으로 받음
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private int age;
	
	@NotNull
	private String userLargeGov;
	
	@NotNull
	private String userSmallGov;
	
	// Response로 User Entity를 가져오는 경우
	public UserDto(User entity) {
		this.userId = entity.getUserId();
		this.email = entity.getEmail();
		this.nickname = entity.getNickname();
		this.realname = entity.getRealName();
		this.point = entity.getPoint();
		this.gender = entity.getGender();
		this.age = entity.getAge();
		this.ageString = Integer.toString(this.age);
		this.userLargeGov = entity.getUserLargeGov();
		this.userSmallGov = entity.getUserSmallGov();
	}
	
}
