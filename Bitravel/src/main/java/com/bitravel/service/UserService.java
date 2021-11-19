package com.bitravel.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.TokenDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Authority;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.jwt.TokenProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final StringRedisTemplate redisTemplate;	

	// 로그인
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public TokenDto getUserAuthentication(LoginDto loginDto) {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());		
		Authentication authentication;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			return null;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		return new TokenDto(jwt);
	}

	// 로그아웃
	@Transactional
	public boolean CancelUserAuthentication (String jwt) {
		// 어차피 내부든 쿠키든 저장이 안되므로 소용없음 다시 리셋됨 (11월 17일 현재 상황 기준)
		if(StringUtils.hasText(jwt) && jwt.startsWith("Bearer "))
			jwt = jwt.substring(7);
		else
			return false;		
		try {
			ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
			logoutValueOperations.set(jwt, jwt);
			User user = (User) tokenProvider.getAuthentication(jwt).getPrincipal();
			log.info("로그아웃 유저 아이디 : '{}', 유저 이름 : '{}'", user.getEmail(),	user.getNickname());
		} catch(Exception e) {
			log.debug("error: "+e);
			return false;
		}
		return true;
	}

	// 회원가입
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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

		log.info(userDto.getPassword());

		User user = User.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.age(userDto.getAge())
				.gender(userDto.getGender())
				.realName(userDto.getRealname())
				.point(0)
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();
		return userRepository.save(user);
	}

	// 회원 이메일로 찾기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Optional<UserDto> getUserWithAuthorities(String email) {
		return userRepository.findOneWithAuthoritiesByEmail(email).map(UserDto::new);
	}

	// 자기 자신 (로그인된 정보) 불러오기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Optional<UserDto> getMyUserWithAuthorities() {
		log.debug("here?");
		//return SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
		// Service logic 점검용
		return userRepository.findOneWithAuthoritiesByEmail(Optional.ofNullable("admin").get()).map(UserDto::new);
	}

	// 아이디(회원 일련번호-PK)로 찾기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Optional<UserDto> getAnyUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.map(UserDto::new);
	}

	// 회원 전체 리스트 불러오기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<UserDto> getAllUserList() {
		Sort sort = Sort.by(Direction.ASC, "userId");
		List<User> list = userRepository.findAll(sort);
		return list.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
	// 회원 닉네임으로 검색한 결과 불러오기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<UserDto> getUserListBynickname(String nickname) {
		// 인기도 등 다른 조건으로 정렬하는 기능 추가할 수 있음
		// Sort sort = Sort.by(Direction.ASC, "userId");
		List<User> list = userRepository.findByNicknameContaining(nickname);
		return list.stream().map(UserDto::new).collect(Collectors.toList());
	}

	// 회원정보 입력받은 내용으로 수정하기
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public User updateUser(UserDto userDto) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail());
		if(userTemp.isEmpty()) {
			return null;
		}
		User user = userTemp.get();
		user.setEmail(userDto.getEmail());
		user.setNickname(userDto.getNickname());
		user.setPoint(userDto.getPoint());
		// View 구현할 때 일반 유저에게 수정을 허용할 내용과 그렇지 않은 내용을 구별해야 함
		user.setGender(userDto.getGender());
		user.setAge(userDto.getAge());
		userRepository.save(user);
		return user;
	}

	// 비밀번호만 수정하기
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean updateUserPassword(String email, String password) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmail(email);
		if(userTemp.isEmpty()) {
			return false;
		}
		User user = userTemp.get();
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return true;
	}

	// 회원정보 삭제하기
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public boolean deleteUser(String email) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmail(email);
		if(userTemp.isEmpty()) {
			return false;
		}
		User user = userTemp.get();
		userRepository.deleteById(user.getUserId());
		return true;
	}
}
