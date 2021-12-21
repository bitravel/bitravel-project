package com.bitravel.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.User;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface UserRepository extends JpaRepository<User, Long>{
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmailAndActivated(String email, Boolean bool);
	
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);
	
	List<User> findByNicknameContainingAndActivated(String keyword, Boolean bool, Sort sort);
	
	List<User> findByEmailContainingAndActivated(String keyword, Boolean bool);
	
	List<User> findByRealNameContainingAndActivated(String keyword, Boolean bool);
	
	Optional<User> findOneByNickname(String keyword);
	
	Page<User> findByNicknameContainingAndActivated(String keyword, Boolean bool, Pageable pageable);
	
	List<User> findByActivated(Boolean bool, Sort sort);
}
