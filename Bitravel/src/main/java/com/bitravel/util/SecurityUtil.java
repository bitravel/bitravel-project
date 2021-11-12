package com.bitravel.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityUtil {
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	public static Optional<String> getCurrentEmail() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null) {
			logger.debug("Security Context에 인증정보 없음");
			return Optional.empty();
		}
		String email = null;
		
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			email = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			email = (String) authentication.getPrincipal();
		}
		return Optional.ofNullable(email);
	}
}
