package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.UserRegion;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface UserRegionRepository extends JpaRepository<UserRegion, Long>{
	
	// 회원별 선호 지역 검색
	List<UserRegion> findByUserEmail(String keyword);
	// 회원별 각 광역단체별 선호 기초자치단체 검색
	List<UserRegion> findByUserEmailAndLargeGov(String userEmail, String largeGov);	
	// 선호 광역자치단체별 회원 검색
	List<UserRegion> findByLargeGov(String keyword);
	// 선호 기초자치단체별 회원 검색
	List<UserRegion> findByLargeGovAndSmallGov(String largeGov, String smallGov);
}
