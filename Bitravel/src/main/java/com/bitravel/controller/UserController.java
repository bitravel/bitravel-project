package com.bitravel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.User;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.jwt.TokenProvider;
import com.bitravel.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
@Api(value = "UserController")
public class UserController {

	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final StringRedisTemplate redisTemplate;

	@PostMapping("/login")
	public String login(@Valid LoginDto loginDto, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		Authentication authentication;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			return "loginPage";
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		//HttpHeaders httpheaders = new HttpHeaders();
		//httpheaders.setConnection("http://localhost:8080");
		// httpheaders.add(JwtFilter.AUTHORIZATION_HEADER, jwt);
		response.setHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
		return "index";
	}

	@PostMapping("/logout")
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		log.info("진입 완료");

		String jwt = response.getHeader(JwtFilter.AUTHORIZATION_HEADER);
		if(StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {jwt = jwt.substring(7); }
		else
			return "index"; // 어차피 내부든 쿠키든 저장이 안되므로 소용없음 다시 리셋됨
		log.info(jwt); ValueOperations<String, String>
		logoutValueOperations = redisTemplate.opsForValue();
		logoutValueOperations.set(jwt, jwt); User user = (User)
				tokenProvider.getAuthentication(jwt).getPrincipal();
		log.info("로그아웃 유저 아이디 : '{}', 유저 이름 : '{}'", user.getEmail(),
				user.getNickname());

		return "redirect:/";
	}

	@PostMapping("/signup")
	public ResponseEntity<User> signup(
			@Valid @RequestBody UserDto userDto
			) {
		return ResponseEntity.ok(userService.signup(userDto));
	}

	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<User> getMyUserInfo() {
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
	}

	//@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/user/{uid}")
	public ResponseEntity<User> getUserInfo(@PathVariable("uid") Long uid) {
		return ResponseEntity.ok(userService.selectUser(uid).get());
	}
}