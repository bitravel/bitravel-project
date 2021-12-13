package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewTravels;

public interface ReviewTravelRepository extends JpaRepository<ReviewTravels, Long>{
		// 회원별 후기 여행지 검색
		List<ReviewTravels> findByReview(Review id);
}
