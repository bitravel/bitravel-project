package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewComment;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long>{
	List<ReviewComment> findAllByReview(Review review);
	List<ReviewComment> deleteAllByReview(Review review);
}
