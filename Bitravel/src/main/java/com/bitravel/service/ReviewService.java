package com.bitravel.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.ReviewRequestDto;
import com.bitravel.data.dto.ReviewResponseDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewTravels;
import com.bitravel.data.entity.Travel;
import com.bitravel.data.entity.User;
import com.bitravel.data.entity.UserTravel;
import com.bitravel.data.repository.ReviewCommentRepository;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.data.repository.ReviewTravelRepository;
import com.bitravel.data.repository.TravelRepository;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.data.repository.UserTravelRepository;
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
	private final UserTravelRepository userTravelRepository;
	private final ReviewCommentRepository rCommentRepository;
	private final ReviewTravelRepository reviewTravelRepository;

	/**
	 * 후기 생성
	 */
	@Transactional
	public Long save(ReviewRequestDto params) {

		// 1. 리뷰 저장
		String nowUserEmail = SecurityUtil.getCurrentEmail().get();
		params.setUserEmail(nowUserEmail);

		if (SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
			params.setNickname("비회원");
		} else {
			User user = userRepository.findOneWithAuthoritiesByEmailAndActivated(nowUserEmail, true).get();
			params.setNickname(user.getNickname());
			params.setUserImage(user.getUserImage());
			user.changePoint(30);
			int age = user.getAge();
			age = age/10;
			if(age==0)
				age=1;
			else if (age>7)
				age=7;
			int ageKey = age*10;
			params.setAge(ageKey);
		}
		Review review = reviewRepository.save(params.toEntity());
		review.getReviewId();

		// 2. 여행지 정보 저장

		// 3. 리뷰-여행지 매핑 정보 저장
		List<ReviewTravels> rt = new ArrayList<>();
		List<UserTravel> ut = new ArrayList<>();
		List<Long> travelIds = params.getTravelId();
		List<String> isLikeds = params.getIsLiked();

		int arr = travelIds.size();
		for (int i=0; i<arr; i++) {
			Travel travelId = travelRepository.getById(travelIds.get(i));
			Review reviewId = reviewRepository.getById(review.getReviewId());
			Boolean isLiked = true;
			if(isLikeds.get(i).equals("0"))
				isLiked = false;
			ReviewTravels entity = ReviewTravels.builder()
					.travel(travelId)
					.review(reviewId)
					.isLiked(isLiked)
					.build();
			rt.add(entity);

			UserTravel ute = UserTravel.builder()
					.userEmail(nowUserEmail)
					.travelId(travelId.getTravelId())
					.isVisited(true)
					.isLiked(isLiked)
					.build();
			ut.add(ute);
		}
		reviewTravelRepository.saveAll(rt);
		userTravelRepository.saveAll(ut);

		return review.getReviewId();
	}
	/**
	 * 후기 여행지 정보 조회
	 */
	@Transactional(readOnly = true)
	public List<ReviewTravels> findByReview(Review id) {
		return reviewTravelRepository.findByReview(id);
	}


	/**
	 * 후기 리스트 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findAll(Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "reviewId"));
		return reviewRepository.findAll(pageable);
	}

	/**
	 * 후기 통합 검색 결과 조회 (Pageable X)
	 */
	@Transactional(readOnly = true)
	public List<Review> findAllForMain() {
		Sort sort = Sort.by(Sort.Direction.DESC, "reviewDate");
		List<Review> all = reviewRepository.findAll(sort);
		if(all.size()>20)
			return all.subList(0, 20);
		else
			return all;
	}

	/**
	 * 특정 여행지 관련 후기 최대 20개 조회
	 */
	@Transactional(readOnly = true)
	public List<Review> find20ByTravel(Long id) {
		List<ReviewTravels> all = reviewTravelRepository.findByTravel(travelRepository.getById(id));
		List<Review> reviews = new ArrayList<>();
		for(int i=0;i<all.size();i++) {
			try {
				reviews.add(reviewRepository.getById(all.get(i).getReview().getReviewId()));
			} catch (Exception e) {
				log.info("삭제된 리뷰");
			}
		}

		Collections.sort(reviews, new Comparator<Review>() {
			@Override
			public int compare(Review o1, Review o2) {
				if(o1.getReviewView()==o2.getReviewView()) {
					return o2.getReviewDate().compareTo(o1.getReviewDate());
				} else {
					return o2.getReviewView()-o1.getReviewView();
				}
			}	
		});

		if(reviews.size()>20)
			return reviews.subList(0, 20);
		else
			return reviews;
	}


	/**
	 * 내가 선호하는 여행지들 후기 조회 (Pageable X)
	 */
	@Transactional(readOnly = true)
	public List<Review> findAllForMe() {
		String myEmail = SecurityUtil.getCurrentEmail().get();
		List<Review> favReviews = new ArrayList<Review>();
		List<UserTravel> myFav = userTravelRepository.findByUserEmailAndIsLiked(myEmail, true);
		LinkedHashSet<Long> duplCheck = new LinkedHashSet<>();
		if(myFav.size()>0) {

			for(int i=0;i<myFav.size();i++) {
				List<ReviewTravels> now = reviewTravelRepository.findByTravel(travelRepository.getById(myFav.get(i).getTravelId()));
				for(int j=0;j<now.size();j++) {
					Review nowR = now.get(j).getReview();
					if(!duplCheck.contains(nowR.getReviewId())) {
						favReviews.add(nowR);
						duplCheck.add(nowR.getReviewId());
					}	
				}
			}
		}

		Collections.sort(favReviews, new Comparator<Review>() {
			@Override
			public int compare(Review o1, Review o2) {
				return o2.getReviewDate().compareTo(o1.getReviewDate());
			}		
		});

		if(favReviews.size()>20)
			return favReviews.subList(0, 20);
		else
			return favReviews;
	}

	/**
	 * 후기 월간 조회수 상위 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findViewAll(Pageable pageable) {
		Date start = new Date(System.currentTimeMillis()-86400000L*30); // 현재 30일 기준
		Date end = new Date();
		pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "reviewView"));
		return reviewRepository.findByReviewDateBetween(start, end, pageable);
	}
	/**
	 * 나이대별 최신 후기 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findAgeAll(int age, Pageable pageable) {
		age = age/10;
		if(age==0)
			age=1;
		else if (age>7)
			age=7;
		int ageKey = age*10;
		pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "reviewDate"));
		return reviewRepository.findByAge(ageKey, pageable);
	}

	/**
	 * 후기 통합 검색 결과 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findReviews(String keyword, Pageable pageable) {

		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "reviewId"));
		return reviewRepository.findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(keyword, keyword, keyword, pageable);
	}

	@Transactional(readOnly = true)
	public List<Review> findReviews(String keyword) {
		Sort sort = Sort.by(Sort.Direction.DESC, "reviewView", "reviewDate");
		return reviewRepository.findByNicknameContainingOrReviewTitleContainingOrReviewContentContaining(keyword, keyword, keyword, sort);
	}

	/**
	 * 후기 닉네임 검색 결과 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findReviewsByNickname(String keyword, Pageable pageable) {
		return reviewRepository.findByNicknameContaining(keyword, pageable);
	}

	/**
	 * 후기 제목 검색 결과 조회
	 */
	@Transactional(readOnly = true)
	public Page<Review> findReviewsByTitle(String keyword, Pageable pageable) {
		return reviewRepository.findByReviewTitleContaining(keyword, pageable);
	}

	/**
	 * 후기 회원별 검색 결과 리스트
	 */
	@Transactional(readOnly = true)
	public List<Review> findReviewsByEmail(String keyword) {
		List<Review> list = reviewRepository.findByUserEmailOrderByReviewDateDesc(keyword);
		return list;
	}

	/**
	 * 후기 제목+내용 검색 결과 조회
	 */
	@Transactional(readOnly = true)
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

		entity.update(params.getReviewTitle(), params.getReviewContent(), params.getThumbNail());
		return true;
	}

	/**
	 * 후기 삭제
	 */
	@Transactional
	public Boolean deleteById(Long id) {
		Review entity = reviewRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
		String nowEmail = SecurityUtil.getCurrentEmail().get();
		// 글쓴이 또는 admin만 수정할 수 있게 함
		if(nowEmail.equals("admin")) {
			log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
		} else if(!entity.getUserEmail().equals(nowEmail)) {
			log.info("유효하지 않은 삭제 요청입니다.");
			return false;
		}
		User user = userRepository.findOneWithAuthoritiesByEmailAndActivated(nowEmail, true).get();
		user.changePoint(-30);
		rCommentRepository.deleteAllByReview(entity);
		reviewRepository.deleteById(id);
		return true;
	}

}
