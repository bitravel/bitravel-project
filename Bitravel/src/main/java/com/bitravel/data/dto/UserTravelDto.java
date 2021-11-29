package com.bitravel.data.dto;

import javax.validation.constraints.NotNull;

import com.bitravel.data.entity.UserTravel;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravelDto {
	 
	// 유저별 관심 지역을 저장하는 Entity	
	@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	private Long userTravelId;
	
	@NotNull
	private String userEmail;
	
	@NotNull
	private String travelId;
	
    private String isVisited; // 방문 여부
    
    private String isLiked; // 선호 여부
    
    private String isWishlisted; // 여행지 찜 여부
	
    public UserTravel toEntity() {
    	boolean visited = false;
    	boolean liked = false;
    	boolean wishlisted = false;
    	if(this.isWishlisted.equals("1")) {
    		wishlisted = true;
    	}
    	if(this.isLiked.equals("1")) {
    		liked = true;
    	}
    	if(this.isVisited.equals("1")) {
    		visited = true;
    	}
    	return UserTravel.builder()
    			.userEmail(userEmail)
    			.travelId(Long.parseLong(travelId))
    			.isLiked(liked)
    			.isVisited(visited)
    			.isWishlisted(wishlisted)
    			.build();
    }
    
}
