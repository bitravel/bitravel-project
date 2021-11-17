package com.bitravel.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bitravel.data.dto.BoardCommentRequestDto;
import com.bitravel.data.dto.BoardCommentResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.BoardComment;
import com.bitravel.data.repository.BoardCommentRepository;
import com.bitravel.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class BoardCommentService {
	
	private final BoardCommentRepository bCommentRepository;
	private BoardService boardService;
	
    /**
     * 특정 게시글의 댓글 모두 보기
     */
    public List<BoardCommentResponseDto> findAllComments(Long bid) {
    	Board board = boardService.detail(bid);
        List<BoardComment> list = bCommentRepository.findAllByBoard(board);
        return list.stream().map(BoardCommentResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 특정 게시물에 댓글 작성
     */
	public Long saveComment(BoardCommentRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	params.setUserEmail(SecurityUtil.getCurrentEmail().get());
    	Board board = boardService.detail(params.getBoardId());
    	params.setBoard(board);
        BoardComment entity = bCommentRepository.save(params.toEntity());
        return entity.getBCommentId();
	}
}
