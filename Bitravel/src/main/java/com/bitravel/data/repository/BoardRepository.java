package com.bitravel.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.dto.BoardResponseDto;
import com.bitravel.data.entity.Board;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface BoardRepository extends JpaRepository<Board, Long>{
	Page<Board> findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(String nickname, String boardTitle, String boardContent, Pageable pageable);

	Page<BoardResponseDto> findByNicknameContaining(String keyword, Pageable pageable);

	Page<BoardResponseDto> findByBoardTitleContaining(String keyword, Pageable pageable);
	
	Page<BoardResponseDto> findByBoardTitleContainingOrBoardContentContaining(String boardTitle, String boardContent, Pageable pageable);
}
