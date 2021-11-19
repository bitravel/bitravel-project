package com.bitravel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitravel.jwt.JwtAuthenticationEntryPoint;
import com.bitravel.jwt.JwtDeniedHandler;
import com.bitravel.jwt.JwtSecurityConfig;
import com.bitravel.jwt.TokenProvider;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtDeniedHandler jwtAccessDeniedHandler;
	
	// 자동으로 랜덤 Salt를 구현해주는 단방향 암호화 클래스 호출 메소드
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// Static path(이미지, CSS 등..)는 보안 과정에서 예외처리해야 함 -> 어떤 접근에서도 사용할 수 있어야 함
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**", "/favicon.ico");
	}
	
	// 권한에 따른 페이지 접근 기능 제어
	// jwt 사용 시 Session을 사용할 필요가 없으므로 STATELESS 설정함 (가능하다면 Session 사용하지 않는 것이 좋음)
	// 인증에 실패한 경우, 권한이 부족한 경우 각각의 EntryPoint나 Handler를 이용하여 response 출력함
	// 다른 사이트나 호스트에서 보낸 요청은 허용하지 않음 (sameOrigin)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.httpBasic().disable()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		.and()
		.headers()
		.frameOptions()
		.sameOrigin()
		
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		
		.and()
		.authorizeRequests() // 바로 다음 antMatchers에 해당하는 url은 인증된 유저만 가능함
		.antMatchers("/couldnotimplement/**").authenticated() // 모든 인증 유저
		.anyRequest().permitAll() // 그 외에 모든 요청은 모든 접근을 허용
		
		.and()
		.apply(new JwtSecurityConfig(tokenProvider)); // 이 모든 권한 설정 검증 등등을 JwtSecurityConfig에서 정의한대로 사용함
	}

}
