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
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**", "/favicon.ico");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
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
		.authorizeRequests()
		.antMatchers("/api/**", "/**").permitAll()
		.anyRequest().authenticated()
		
		.and()
		.apply(new JwtSecurityConfig(tokenProvider));
	}

}
