package com.bitravel.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.bitravel.data.entity.Board;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface BoardRepository extends CrudRepository<Board, Long>{
}
