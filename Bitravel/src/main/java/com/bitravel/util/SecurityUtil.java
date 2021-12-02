package com.bitravel.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bitravel.data.entity.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SecurityUtil {

	// 현재 웹사이트에 로그인한 유저의 이메일을 얻는 메소드
	public static Optional<String> getCurrentEmail() {
		// 현재 웹사이트의 인증정보를 확인
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication == null) {
			log.debug("Security Context에 인증정보 없음");
			return Optional.empty();
		}
		String email = null;
		log.info(authentication.getPrincipal().toString());
		
		// Spring Security를 구현하기 위해서는 user class가 user details도 구현할 수 있어야 함 (구현 가능)
		// 인증 결과로 저장하고 있는 값이 class이면 거기에서 따로 메일을 꺼내고, String이면 바로 이메일로 적용함
		if(authentication.getPrincipal() instanceof User) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			email = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			email = (String) authentication.getPrincipal();
		}
		return Optional.ofNullable(email);
	}
}
