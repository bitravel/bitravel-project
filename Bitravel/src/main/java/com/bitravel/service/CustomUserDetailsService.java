package com.bitravel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bitravel.data.entity.User;
import com.bitravel.data.repository.UserRepository;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		Optional<User> opt = userRepository.findOneWithAuthoritiesByEmail(email);
		
		if(opt.isEmpty()) {
			throw new UsernameNotFoundException(email+"-> DB에서 사용자 정보를 찾을 수 없음");
		} 
		User user = opt.get();
		return createUser(email, user);
	}
	
	private org.springframework.security.core.userdetails.User createUser(String email, User user) {
		if(!user.isActivated()) {
			throw new RuntimeException(email+"-> 활성화되지 않은 계정임");
		}
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getRoleName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}

}
