package com.bitravel.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.ReviewCommentRequestDto;
import com.bitravel.data.dto.ReviewCommentResponseDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewComment;
import com.bitravel.data.repository.ReviewCommentRepository;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewCommentService {
	
	private final ReviewCommentRepository rCommentRepository;
	private final UserRepository userRepository;
	private ReviewService reviewService;
	
    /**
     * 특정 후기의 댓글 모두 보기
     */
    public List<ReviewCommentResponseDto> findAllComments(Long bid) {
    	Review review = reviewService.detail(bid);
        List<ReviewComment> list = rCommentRepository.findAllByReview(review);
        return list.stream().map(ReviewCommentResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 특정 후기에 댓글 작성
     */
	public Long saveComment(ReviewCommentRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	String nowUserEmail = SecurityUtil.getCurrentEmail().get();
    	params.setUserEmail(nowUserEmail);
    	if (SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
    		params.setNickname("비회원");
    	} else {
    		params.setNickname(userRepository.findOneWithAuthoritiesByEmail(nowUserEmail).get().getNickname());
    	}
    	Review review = reviewService.detail(params.getReviewId());
    	params.setReview(review);
        ReviewComment entity = rCommentRepository.save(params.toEntity());
        return entity.getRCommentId();
	}
	
    /**
     * 댓글 수정
     */
    @Transactional
    public Boolean update(final Long id, final ReviewCommentRequestDto params) {
        ReviewComment entity = rCommentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 수정합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 수정 요청입니다.");
        	return false;
        }
        entity.update(params.getCommentContent());
        return true;
    }
    
    /**
     * 댓글 삭제
     */
    @Transactional
    public Boolean deleteById(Long id) {
        ReviewComment entity = rCommentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 삭제 요청입니다.");
        	return false;
        }
    	rCommentRepository.deleteById(id);
    	return true;
    }
}
