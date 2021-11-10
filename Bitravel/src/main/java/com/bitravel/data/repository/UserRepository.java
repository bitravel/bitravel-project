package com.bitravel.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bitravel.data.entity.User;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface UserRepository extends CrudRepository<User, String>{
	@Query("select u from User u where u.userId = :id")
	User findUser(@Param("id") String inputid);
}
