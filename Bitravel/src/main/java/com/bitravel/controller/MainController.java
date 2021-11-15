package com.bitravel.controller;

import javax.validation.Valid;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.TokenDto;
import com.bitravel.data.entity.User;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.jwt.TokenProvider;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
@Api(value = "MainController")
public class MainController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final StringRedisTemplate redisTemplate;

	@PostMapping("/login")
	public ResponseEntity<TokenDto> authorize(@Valid LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.createToken(authentication);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
	}

	/*
	 * @Transactional
	 * 
	 * @PostMapping("/logout") public ModelAndView logout(String jwt) {
	 * ValueOperations<String, String> logoutValueOperations =
	 * redisTemplate.opsForValue(); logoutValueOperations.set(jwt, jwt); User user =
	 * (User) tokenProvider.getAuthentication(jwt).getPrincipal();
	 * log.info("로그아웃 유저 아이디 : '{}', 유저 이름 : '{}'", user.getEmail(),
	 * user.getNickname()); ModelAndView mav = new ModelAndView();
	 * mav.setViewName("redirect:/"); return mav; }
	 */
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody String jwt) {
		log.info("진입 완료");
//		String jwt = tokenInfo.getToken();
		ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
		logoutValueOperations.set(jwt, jwt);
		User user = (User) tokenProvider.getAuthentication(jwt).getPrincipal();
		log.info("로그아웃 유저 아이디 : '{}', 유저 이름 : '{}'", user.getEmail(), user.getNickname());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "");
		return new ResponseEntity<>(new TokenDto(""), httpHeaders, HttpStatus.OK);
	}
}