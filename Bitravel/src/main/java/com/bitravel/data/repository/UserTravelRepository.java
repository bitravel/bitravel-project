package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.UserTravel;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface UserTravelRepository extends JpaRepository<UserTravel, Long>{
	
	// 회원별 여행지 관련 정보 검색
	List<UserTravel> findByUserEmail(String keyword);
	// 회원별 방문 여행지 관련 정보 검색
	List<UserTravel> findByUserEmailAndIsVisited(String userEmail, Boolean isVisited);
	// 회원별 선호 여행지 관련 정보 검색
	List<UserTravel> findByUserEmailAndIsLiked(String userEmail, Boolean isLiked);
	// 회원별 찜 여행지 관련 정보 검색
	List<UserTravel> findByUserEmailAndIsWishlisted(String userEmail, Boolean isWishlisted);
	// 여행지별 방문/미방문 회원 관련 정보 검색
	List<UserTravel> findByTravelIdAndIsVisited(String travelId, Boolean isVisited);	
	// 여행지별 전체 회원 관련 정보 검색
	List<UserTravel> findByTravelId(String travelId);	

}
