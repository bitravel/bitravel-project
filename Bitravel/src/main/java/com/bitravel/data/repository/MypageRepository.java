package com.bitravel.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.User;

public interface MypageRepository extends JpaRepository<User, Long>{
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);
	
	List<User> findByNicknameContaining(String keyword, Sort sort);
	
	List<User> findByEmailContaining(String keyword);
	
	List<User> findByRealNameContaining(String keyword);
	
	Optional<User> findOneByNickname(String keyword);
	
	Page<User> findByNicknameContaining(String keyword, Pageable pageable);
	
}
