package com.bitravel.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.User;

public interface MypageRepository extends JpaRepository<User, Long>{
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);
		
	@Modifying
	@Transactional
	@Query(value = " update                                 "
				+  "     user                               "
				+  " set                                    "
				+  "     password = :#{#user.password}      "
				+  " where                                  "
				+  "     user_id  = :#{#user.userId}        "
				, nativeQuery = true)
	public int updatePassword(@Param(value = "user") UserDto user);
	
	
}


