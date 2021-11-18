package com.bitravel.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.ReviewRequestDto;
import com.bitravel.data.dto.ReviewResponseDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.Travel;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.data.repository.TravelRepository;
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
    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(ReviewRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	params.setUserEmail(SecurityUtil.getCurrentEmail().get());
    	Set<Travel> travelList = new HashSet<>();
    	List<Long> travelIds = params.getTravelId();
    	int L = travelIds.size();
    	for(int i=0; i<L; i++) {
    		travelList.add(travelRepository.getById(travelIds.get(i)));
    	}
    	params.setTravel(travelList);
        Review entity = reviewRepository.save(params.toEntity());
        return entity.getReviewId();
    }

    /**
     * 게시글 리스트 조회
     */
    public List<ReviewResponseDto> findAll() {

        Sort sort = Sort.by(Direction.DESC, "reviewId", "reviewDate");
        List<Review> list = reviewRepository.findAll(sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }
    
    /**
     * 게시글 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public Review detail(Long id) {
    	return reviewRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    }

    /**
     * 게시글 수정
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
    	Set<Travel> travelList = new HashSet<>();
    	List<Long> travelIds = params.getTravelId();
    	int L = travelIds.size();
    	for(int i=0; i<L; i++) {
    		travelList.add(travelRepository.getById(travelIds.get(i)));
    	}
        entity.update(params.getReviewTitle(), params.getReviewContent(), travelList);
        return true;
    }
    
    /**
     * 게시글 삭제
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
    	reviewRepository.deleteById(id);
    	return true;
    }
    
}
