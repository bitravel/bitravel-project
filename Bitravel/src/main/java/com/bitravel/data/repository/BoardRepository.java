package com.bitravel.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.Board;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface BoardRepository extends JpaRepository<Board, Long>{
	Page<Board> findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(String nickname, String boardTitle, String boardContent, Pageable pageable);

	Page<Board> findByNicknameContaining(String keyword, Pageable pageable);
	
	Optional<Board> findByUserEmail(String useremail);

	Page<Board> findByBoardTitleContaining(String keyword, Pageable pageable);
	
	Page<Board> findByBoardTitleContainingOrBoardContentContaining(String boardTitle, String boardContent, Pageable pageable);

	List<Board> findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(String nickname, String boardTitle,
			String boardContent, Sort sort);
}
