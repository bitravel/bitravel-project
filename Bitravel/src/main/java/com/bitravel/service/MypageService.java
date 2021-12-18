package com.bitravel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.MypageRepository;
import com.bitravel.data.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MypageService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private MypageRepository mypageRepository;

	// 회원정보 수정
	@Transactional()
	public User updateUser(UserDto userDto) {
		User user = userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).get();
		
		user.setGender(      userDto.getGender());
		user.setNickname(    userDto.getNickname());
		user.setUserLargeGov(userDto.getUserLargeGov());
		user.setUserSmallGov(userDto.getUserSmallGov());
		
		return mypageRepository.save(user);
	}

	// 비밀번호 수정
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean updateUserPassword(String email, String password) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmail(email);
		
		User user = userTemp.get();
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return true;
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
