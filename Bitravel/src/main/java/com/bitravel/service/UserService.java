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
import com.bitravel.data.entity.UserTravel;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.data.repository.UserTravelRepository;
import com.bitravel.jwt.TokenProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private UserTravelRepository userTravelRepository;
	private PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final StringRedisTemplate redisTemplate;	

	// 로그인
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public TokenDto getUserAuthentication(LoginDto loginDto) {
		// 로그인 Dto가 들어가면 Dto에 있는 정보들로 이메일-비번 확인 클래스가 확인 시작
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		
		// 위의 클래스에서 확인한 결과를 기반으로 인증 클래스가 인증 결과 반환
		Authentication authentication;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			return null;
		}
		
		// 현재 브라우저에 인증 결과를 바탕으로 권한 설정함 (Admin / user 등)
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// 현재 회원임을 증명할 수 있는 토큰 생성
		String jwt = tokenProvider.createToken(authentication);
		
		// 해당 토큰을 Client(Broswer)에게 반환
		return new TokenDto(jwt);
	}

	// 로그아웃
	@Transactional
	public boolean CancelUserAuthentication (String jwt) {
		// 어차피 내부든 쿠키든 저장이 안되므로 소용없음 다시 리셋됨 (11월 17일 현재 상황 기준)
		
		// 유저에게 받아온 토큰 맨 앞자리의 'Bearer ' 문자열 삭제
		if(StringUtils.hasText(jwt) && jwt.startsWith("Bearer "))
			jwt = jwt.substring(7);
		else {
			log.info("유효한 header를 찾을 수 없음");
			return false;
		}
					
		try {
			// 로그아웃 후에는 해당 토큰 만료 전이라도 인증할 수 없게 해야 하므로 redis의 예외 테이블에 map 형태로 저장함
			// redis에서는 유효기간 이후에 토큰이 저절로 사라짐
			ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
			// 검색이 편리하도록 key와 value를 동일하게 설정
			logoutValueOperations.set(jwt, jwt);
			
			// 이 토큰을 쓰고 있던 유저를 검색함 (필수는 아니고 logout 성공 여부 확인용)
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
		// 이메일 기준으로 회원 중복여부 확인
		if(userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 회원입니다.");
		}
		
		// 회원 맞는 경우 관리자인지 확인 
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
		
		// 암호화 시작 전 회원의 비밀번호 확인
		log.info(userDto.getPassword());
		
		// user dto의 내용을 토대로 entity 생성
		User user = User.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.age(Integer.parseInt(userDto.getAgeString()))
				.gender(userDto.getGender())
				.realName(userDto.getRealname())
				.point(0)
				.userLargeGov(userDto.getUserLargeGov())
				.userSmallGov(userDto.getUserSmallGov())
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();
		return userRepository.save(user);
	}
	
	/**
	 * UserTravel 신규 등록
	 */
	@Transactional
	public UserTravel saveUserTravel(UserTravel param) {		
		return userTravelRepository.save(param);
	}
	
	// 회원 이메일로 찾기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Optional<UserDto> getUserWithAuthorities(String email) {
		return userRepository.findOneWithAuthoritiesByEmail(email).map(UserDto::new);
	}
	
	// 회원 닉네임으로 찾기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Optional<UserDto> getUserByNickname(String nickname) {
		return userRepository.findOneByNickname(nickname).map(UserDto::new);
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
	
	// 회원 본명으로 검색한 결과 불러오기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<UserDto> getUserListByrealname(String realname) {
		// 인기도 등 다른 조건으로 정렬하는 기능 추가할 수 있음
		// Sort sort = Sort.by(Direction.ASC, "userId");
		List<User> list = userRepository.findByRealNameContaining(realname);
		return list.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
	// 회원 이메일으로 검색한 결과 불러오기
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<UserDto> getUserListByEmail(String email) {
		// 인기도 등 다른 조건으로 정렬하는 기능 추가할 수 있음
		// Sort sort = Sort.by(Direction.ASC, "userId");
		List<User> list = userRepository.findByEmailContaining(email);
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
	
	// 회원정보 삭제하기
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public boolean deleteUserByList(List<Long> list) {
		try {
			userRepository.deleteAllById(list);
		} catch (IllegalArgumentException e) {
			log.info("올바르지 않은 삭제 요청");
			return false;
		}
		return true;
	}
	
	// 포인트만 수정하기
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean updatePoint(String email, Integer point) {
		Optional<User> userTemp = userRepository.findOneWithAuthoritiesByEmail(email);
		if(userTemp.isEmpty()) {
			return false;
		}
		User user = userTemp.get();
		user.setPoint(point);
		userRepository.save(user);
		return true;
	}
	
}
