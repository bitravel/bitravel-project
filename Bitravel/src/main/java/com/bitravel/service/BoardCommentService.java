package com.bitravel.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.BoardCommentRequestDto;
import com.bitravel.data.dto.BoardCommentResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.BoardComment;
import com.bitravel.data.repository.BoardCommentRepository;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class BoardCommentService {
	
	private final BoardCommentRepository bCommentRepository;
	private final UserRepository userRepository;
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
    	String nowUserEmail = SecurityUtil.getCurrentEmail().get();
    	params.setUserEmail(nowUserEmail);
    	if (SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
    		params.setNickname("비회원");
    	} else {
    		params.setNickname(userRepository.findOneWithAuthoritiesByEmail(nowUserEmail).get().getNickname());
    	}
    	Board board = boardService.detail(params.getBoardId());
    	params.setBoard(board);
        BoardComment entity = bCommentRepository.save(params.toEntity());
        return entity.getBCommentId();
	}
	
    /**
     * 댓글 수정
     */
    @Transactional
    public Boolean update(final Long id, final BoardCommentRequestDto params) {
        BoardComment entity = bCommentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 수정합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 수정 요청입니다.");
        	return false;
        }
        entity.update(params.getCommentContent());
        return true;
    }
    
    /**
     * 댓글 삭제
     */
    @Transactional
    public Boolean deleteById(Long id) {
        BoardComment entity = bCommentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 삭제 요청입니다.");
        	return false;
        }
    	bCommentRepository.deleteById(id);
    	return true;
    }
}
