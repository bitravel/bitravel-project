package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Review;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface ReviewRepository extends JpaRepository<Review, Long>{
	Page<Review> findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(String nickname, String reviewTitle, String reviewContent, Pageable pageable);
	
	List<Review> findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(String nickname, String reviewTitle, String reviewContent, Sort sort);
	
	Page<Review> findByNicknameContaining(String keyword, Pageable pageable);

	Page<Review> findByReviewTitleContaining(String keyword, Pageable pageable);
	
	Page<Review> findByReviewTitleContainingOrReviewContentContaining(String reviewTitle, String reviewContent, Pageable pageable);
}
