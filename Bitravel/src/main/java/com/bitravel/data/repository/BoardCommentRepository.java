package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.BoardComment;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>{
	List<BoardComment> findAllByBoard(Board board);
}
//public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>{
//List<BoardComment> findAllByBoard(Board board, Pageable pageable);
//}
