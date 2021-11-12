package com.bitravel.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.TokenDto;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.jwt.TokenProvider;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Api(value = "MainController")
public class MainController {
 
  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  
  
  @PostMapping("/login")
  public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
	  
	  UsernamePasswordAuthenticationToken authenticationToken = 
			  new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
	  Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	  SecurityContextHolder.getContext().setAuthentication(authentication);
	  
	  String jwt = tokenProvider.createToken(authentication);
	  
	  HttpHeaders httpHeaders = new HttpHeaders();
	  httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
	  return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
  }
  
  
  
}