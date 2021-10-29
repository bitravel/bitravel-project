package com.bitravel.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.bitravel.data.entity.Test;
 
public interface TestRepository extends CrudRepository<Test, Long>{
}
