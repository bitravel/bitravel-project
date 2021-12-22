package com.bitravel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.UserUpdateDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.BoardRepository;
import com.bitravel.data.repository.MypageRepository;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.data.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MypageService {

	private UserRepository userRepository;
	private MypageRepository mypageRepository;
	private final ReviewRepository reviewRepository;
	private final BoardRepository boardRepository;


	// 회원정보 수정
	@Transactional
	public User updateUser(UserUpdateDto userDto) {
		log.info(userDto.getEmail());
		User user = userRepository.findOneWithAuthoritiesByEmailAndActivated(userDto.getEmail(), true).get();

		user.setNickname(    userDto.getNickname());
		user.setUserLargeGov(userDto.getUserLargeGov());
		user.setUserSmallGov(userDto.getUserSmallGov());
		user.setUserImage(userDto.getUserImage());

		return mypageRepository.save(user);
	}

	//내가 작성한 후기리뷰 리스트 조회
	@Transactional (readOnly=true)
	public List<Review> getMyReview(String userEmail) {
		List<Review> myReview = new ArrayList<Review>();
		myReview = reviewRepository.findByUserEmailOrderByReviewDateDesc(userEmail);
		return myReview;
	}

	//내가 작성한 게시글 리스트 조회
	@Transactional (readOnly=true)
	public List<Board> getMyBoard(String keyword) {
		List<Board> myBoard = new ArrayList<Board>();
		myBoard = boardRepository.findByUserEmailOrderByBoardDateDesc(keyword);
		return myBoard;
	}
}
