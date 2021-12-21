package com.bitravel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.controller.UserController;
import com.bitravel.data.dto.UserUpdateDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.User;
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
	private PasswordEncoder passwordEncoder;
	private MypageRepository mypageRepository;
	private final ReviewRepository reviewRepository;

	// 회원정보 수정
	@Transactional()
	public User updateUser(UserUpdateDto userDto) {
		log.info(userDto.getEmail());
		User user = userRepository.findOneWithAuthoritiesByEmailAndActivated(userDto.getEmail(), true).get();

		user.setNickname(    userDto.getNickname());
		user.setUserLargeGov(userDto.getUserLargeGov());
		user.setUserSmallGov(userDto.getUserSmallGov());
		user.setUserImage(userDto.getUserImage());
		
		return mypageRepository.save(user);
	}

	// 비밀번호 수정
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean updateUserPassword(String email, String password) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmailAndActivated(email, true);
		
		User user = userTemp.get();
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return true;
	}

	//내가 작성한 리뷰 리스트 조회
	@Transactional
	public List<Review> getMyReview(String userEmail) {
		List<Review> myReview = new ArrayList<Review>();
		myReview = reviewRepository.findByUserEmailOrderByReviewDateAsc(userEmail);
		return myReview;
	}
	
	
//	@Autowired
//	PasswordEncoder passwordEncoder1;
//	public User save(User user, String role, String type) {
//			// TODO Auto-generated method stub
//			user.setPassword(passwordEncoder.encode(user.getPassword()));
//			user.setuserNonExpired(true);
//			user.setuserNonLocked(true);
//			user.setCredentialsNonExpired(true);
//			user.setEnabled(true);
//			user.setType(type);
//			return user.save(user, role);
//		}
}
