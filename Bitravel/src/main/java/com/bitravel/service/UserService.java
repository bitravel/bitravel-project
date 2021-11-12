package com.bitravel.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Authority;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.util.SecurityUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService /* implements UserDetailsService */ {
    
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public User signup(UserDto userDto) {
		if(userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 회원입니다.");
		}	
		Authority authority;
		if(userDto.getEmail().equals("admin")) {
			authority = Authority.builder()
					.roleName("ROLE_ADMIN")
					.build();
		} else {
			authority = Authority.builder()
				.roleName("ROLE_USER")
				.build();
		}
		
		User user = User.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.age(userDto.getAge())
				.gender(userDto.getGender())
				.realName(userDto.getRealname())
				.authorities(Collections.singleton(authority))
				.point(0)
				.activated(true)
				.build();
		return userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities(String email) {
		return userRepository.findOneWithAuthoritiesByEmail(email);
	}
	
	@Transactional(readOnly = true)
	public Optional<User> getMyUserWithAuthorities() {
		return SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
	}
}