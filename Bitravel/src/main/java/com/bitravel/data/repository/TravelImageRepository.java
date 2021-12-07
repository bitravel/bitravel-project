package com.bitravel.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.TravelImage;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface TravelImageRepository extends JpaRepository<TravelImage, Long>{
	Optional<TravelImage> findByIsUpdatedAndTravelName(Boolean keyword, String travelName);
}
