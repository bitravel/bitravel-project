package com.bitravel.data.dto;

import com.bitravel.data.entity.User;

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
public class UserUpdateDto {
	
	private String email;
	
	private String nickname;
	
	private String userImage;
	
	private String userLargeGov;
	
	private String userSmallGov;
	
	// Response로 User Entity를 가져오는 경우
	public UserUpdateDto(User entity) {
		this.email = entity.getEmail();
		this.nickname = entity.getNickname();
		this.userLargeGov = entity.getUserLargeGov();
		this.userSmallGov = entity.getUserSmallGov();
		this.userImage = entity.getUserImage();
	}
	
}
