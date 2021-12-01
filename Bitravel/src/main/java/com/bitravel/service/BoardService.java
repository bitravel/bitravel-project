package com.bitravel.service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.BoardRequestDto;
import com.bitravel.data.dto.BoardResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.repository.BoardRepository;
import com.bitravel.data.repository.UserRepository;
import com.bitravel.exception.CustomException;
import com.bitravel.exception.ErrorCode;
import com.bitravel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	
    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(BoardRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	String nowUserEmail = SecurityUtil.getCurrentEmail().get();
    	params.setUserEmail(nowUserEmail);
    	if (SecurityUtil.getCurrentEmail().get().equals("anonymousUser")) {
    		params.setNickname("비회원");
    	} else {
    		params.setNickname(userRepository.findOneWithAuthoritiesByEmail(nowUserEmail).get().getNickname());
    	}
        Board entity = boardRepository.save(params.toEntity());
        return entity.getBoardId();
    }

    /**
     * 게시글 리스트 조회
     */
    public Page<Board> findAll(Pageable pageable) {
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
    	return boardRepository.findAll(pageable);
    }
    
    /**
     * 게시글 통합 검색 결과 조회
     */
    @Transactional
    public Page<Board> findBoards(String keyword, Pageable pageable) {

    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
        return boardRepository.findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(keyword, keyword, keyword, pageable);
    }
    
    /**
     * 게시글 닉네임 검색 결과 조회
     */
    @Transactional
    public Page<BoardResponseDto> findBoardsByNickname(String keyword, Pageable pageable) {
        return boardRepository.findByNicknameContaining(keyword, pageable);
    }
    
    /**
     * 게시글 제목 검색 결과 조회
     */
    @Transactional
    public Page<BoardResponseDto> findBoardsByTitle(String keyword, Pageable pageable) {
        return boardRepository.findByBoardTitleContaining(keyword, pageable);
    }
    
    /**
     * 게시글 제목+내용 검색 결과 조회
     */
    @Transactional
    public Page<BoardResponseDto> findBoardsByTitleAndContent(String keyword, Pageable pageable) {
        return boardRepository.findByBoardTitleContainingOrBoardContentContaining(keyword, keyword, pageable);
    }
    
    /**
     * 게시글 상세 정보 조회
     */
    @Transactional
    public BoardResponseDto detail(Long id) {
    	Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    	entity.increaseView();
    	return new BoardResponseDto(entity);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Boolean update(final Long id, final BoardRequestDto params) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 수정합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 수정 요청입니다.");
        	return false;
        }
        entity.update(params.getBoardTitle(), params.getBoardContent());
        return true;
    }
    
    /**
     * 게시글 삭제
     */
    @Transactional
    public Boolean deleteById(Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 글쓴이 또는 admin만 수정할 수 있게 함
        if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
        	log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
        	log.info("유효하지 않은 삭제 요청입니다.");
        	return false;
        }
    	boardRepository.deleteById(id);
    	return true;
    }
    
}
