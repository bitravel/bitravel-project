package com.bitravel.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.ReviewRequestDto;
import com.bitravel.data.dto.ReviewResponseDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.Travel;
import com.bitravel.data.repository.ReviewCommentRepository;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.data.repository.TravelRepository;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final ReviewCommentRepository rCommentRepository;
	
    /**
     * 후기 생성
     */
    @Transactional
    public Long save(ReviewRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	String nowUserEmail = SecurityUtil.getCurrentEmail().get();
    	params.setUserEmail(nowUserEmail);
    	if (SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
    		params.setNickname("비회원");
    	} else {
    		params.setNickname(userRepository.findOneWithAuthoritiesByEmail(nowUserEmail).get().getNickname());
    	}
    	Set<Travel> travelSet = new HashSet<>();
    	List<Long> travelIds = params.getTravelId();
    	int L = travelIds.size();
    	for(int i=0; i<L; i++) {
    		travelSet.add(travelRepository.getById(travelIds.get(i)));
    	}
    	params.setTravel(travelSet);
        Review entity = reviewRepository.save(params.toEntity());
        return entity.getReviewId();
    }

    /**
     * 후기 리스트 조회
     */
    public Page<Review> findAll(Pageable pageable) {
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "reviewId"));
		return reviewRepository.findAll(pageable);
    }
    
    /**
     * 후기 통합 검색 결과 조회
     */
    @Transactional
    public Page<Review> findReviews(String keyword, Pageable pageable) {

    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "reviewId"));
        return reviewRepository.findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(keyword, keyword, keyword, pageable);
    }
    
    /**
     * 후기 닉네임 검색 결과 조회
     */
    @Transactional
    public Page<Review> findReviewsByNickname(String keyword, Pageable pageable) {
    	return reviewRepository.findByNicknameContaining(keyword, pageable);
    }
    
    /**
     * 후기 제목 검색 결과 조회
     */
    @Transactional
    public Page<Review> findReviewsByTitle(String keyword, Pageable pageable) {
    	return reviewRepository.findByReviewTitleContaining(keyword, pageable);
    }
    
    /**
     * 후기 제목+내용 검색 결과 조회
     */
    @Transactional
    public Page<Review> findReviewsByTitleAndContent(String keyword, Pageable pageable) {
    	return reviewRepository.findByReviewTitleContainingOrReviewContentContaining(keyword, keyword, pageable);
    }
    
    /**
     * 후기 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public ReviewResponseDto detail(Long id) {
    	Review entity = reviewRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    	entity.increaseView();
    	return new ReviewResponseDto(entity);
    }

    /**
     * 후기 수정
     */
    @Transactional
    public Boolean update(final Long id, final ReviewRequestDto params) {
        Review entity = reviewRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 수정합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 수정 요청입니다.");
        	return false;
        }
    	Set<Travel> travelSet = new HashSet<>();
    	List<Long> travelIds = params.getTravelId();
    	int L = travelIds.size();
    	for(int i=0; i<L; i++) {
    		travelSet.add(travelRepository.getById(travelIds.get(i)));
    	}
        entity.update(params.getReviewTitle(), params.getReviewContent(), travelSet);
        return true;
    }
    
    /**
     * 후기 삭제
     */
    @Transactional
    public Boolean deleteById(Long id) {
        Review entity = reviewRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 삭제 요청입니다.");
        	return false;
        }
        rCommentRepository.deleteAllByReview(entity);
    	reviewRepository.deleteById(id);
    	return true;
    }
    
}
