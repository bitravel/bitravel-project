package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Travel;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface TravelRepository extends JpaRepository<Travel, Long>{
	// 제목으로 여행지 검색 (정렬 가능)
	List<Travel> findByTravelNameContaining(String keyword, Sort sort);
	Page<Travel> findByTravelNameContaining(String keyword, Pageable pageable);
	// 제목으로 여행지 검색 Containing = like
	List<Travel> findByTravelNameContaining(String name);
	// 정확히 같은 여행지를 찾는 경우도 추가 (이미지 호출용)
	List<Travel> findByTravelName(String name);
	// 광역자치단체로 여행지 검색 (정렬 가능)
	Page<Travel> findByLargeGov(String keyword, Pageable pageable);
	// 광역자치단체로 여행지 검색
	List<Travel> findByLargeGov(String keyword);
	// 기초자치단체로 여행지 검색 (정렬 가능)
	Page<Travel> findByLargeGovAndSmallGov(String largeGov, String smallGov, Pageable pageable);
	List<Travel> findByLargeGovAndSmallGov(String largeGov, String smallGov, Sort sort);
	// 기초자치단체로 여행지 검색
	List<Travel> findByLargeGovAndSmallGov(String largeGov, String smallGov);
}
