package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Region;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface RegionRepository extends JpaRepository<Region, Long>{	
	
	// 광역자치단체 별 리스트 검색
	List<Region> findByLargeGov(String keyword);
	// 전체 기초자치단체 검색
	List<Region> findByLargeGovAndSmallGov(String largeGov, String smallGov);
}
