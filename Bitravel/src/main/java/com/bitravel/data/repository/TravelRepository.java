package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Travel;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface TravelRepository extends JpaRepository<Travel, Long>{
	// 제목으로 여행지 검색
	List<Travel> findByTravelNameContaining(String keyword);
	// 광역자치단체로 여행지 검색
	List<Travel> findByLargeGov(String keyword);
	// 기초자치단체로 여행지 검색
	List<Travel> findByLargeGovAndSmallGov(String largeGov, String smallGov);
}
