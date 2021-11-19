package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Review;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(String nickname, String reviewTitle, String reviewContent);

	List<Review> findByNicknameContaining(String keyword);

	List<Review> findByReviewTitleContaining(String keyword);
	
	List<Review> findByReviewTitleContainingOrReviewContentContaining(String reviewTitle, String reviewContent);
}
