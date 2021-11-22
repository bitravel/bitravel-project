package com.bitravel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Authority;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.jwt.TokenProvider;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private TokenProvider tokenProvider;
	
	@Mock
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Mock
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	public void saveNewUser() {
		Set<Authority> set = new HashSet<>();
		set.add(new Authority("USER_ADMIN"));
		
		// given
		User user = User.builder().userId((long) 1)
				.age(30).email("space11kay@kakao.com")
				.gender("Woman").nickname("케이")
				.password("space").point(0).realName("김종희")
				.authorities(set)
				.build();
		
		when(userRepository.save(any())).thenReturn(user);
		
		// when
		User result = 
				userService.signup(UserDto.builder().email("spacekay@kakao.com").build());
		
		// then
		verify(userRepository, times(1)).save(any());
		assertThat (result.getEmail().equals(user.getEmail()));
	}
	
	@Test
	public void findUserWithEmail() {
		//given
		Optional<User> user = Optional.ofNullable(User.builder().email("spacekay@kakao.com").build());
		
		when(userRepository.findOneWithAuthoritiesByEmail("spacekay@kakao.com")).thenReturn(user);
		
		Optional<UserDto> result = userService.getUserWithAuthorities("spacekay@kakao.com");
		
		verify(userRepository, times(1)).findOneWithAuthoritiesByEmail("spacekay@kakao.com");
		assertThat(result.get().getEmail().equals(user.get().getEmail()));
	}

}
