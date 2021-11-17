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
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long userId;
	
	@NotNull
	@Size(min=5, max=50)
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
    @Size(min=4, max=100)
	private String password; // 회원가입 시에만 사용함. 그 외에는 외부로 유출되지 않음
	
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
	
	// Response로 User Entity를 가져오는 경우
	public UserDto(User entity) {
		this.userId = entity.getUserId();
		this.email = entity.getEmail();
		this.nickname = entity.getNickname();
		this.realname = entity.getRealName();
		this.point = entity.getPoint();
		this.gender = entity.getGender();
		this.age = entity.getAge();
	}
	
}
