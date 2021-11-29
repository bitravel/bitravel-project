package com.bitravel.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {
	
	// Http header의 attribute 이름 (ex> 'Authorization' : 'jwt')
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	private TokenProvider tokenProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Client의 request에서 token을 뽑아냄
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI();
		
		// token에 내용이 있는지, 그리고 이 토큰이 유효한지 검증함
		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			// 검증한 결과에 따른 인증 결과를 사이트 구현에 반영함
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context에 '{}' 인증정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
		} else {
			log.debug("유효한 jwt 토큰이 없습니다, uri: {}", requestURI);
		}
		
		// 반영한 결과를 기반으로 변경사항 없을 시 request와 response에 인증 정보를 유지하도록 함
		chain.doFilter(request, response);
	}
	
	// 토큰에서 앞부분 Bearer라는 문자열을 삭제함
	private String resolveToken(HttpServletRequest request) {
		String[] Cookies = request.getHeader("Cookie").split("; ");
		String bearerToken = Cookies[1].replace(AUTHORIZATION_HEADER, "");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("=Bearer ")) {
			return bearerToken.substring(8);
		}
		return null;
	}

}
