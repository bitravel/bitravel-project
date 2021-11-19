package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Board;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface BoardRepository extends JpaRepository<Board, Long>{
	List<Board> findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(String nickname, String boardTitle, String boardContent);

	List<Board> findByNicknameContaining(String keyword);

	List<Board> findByBoardTitleContaining(String keyword);
	
	List<Board> findByBoardTitleContainingOrBoardContentContaining(String boardTitle, String boardContent);
}
