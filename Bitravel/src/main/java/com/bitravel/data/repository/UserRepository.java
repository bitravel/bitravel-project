package com.bitravel.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.bitravel.data.entity.User;
 
public interface UserRepository extends CrudRepository<User, Long>{
}
