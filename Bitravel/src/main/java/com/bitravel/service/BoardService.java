package com.bitravel.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.BoardRequestDto;
import com.bitravel.data.dto.BoardResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.repository.BoardRepository;
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
	
    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(BoardRequestDto params) {
    	// JWT 구현 전에는 anonymousUser로 기록됨
    	params.setUserEmail(SecurityUtil.getCurrentEmail().get());
        Board entity = boardRepository.save(params.toEntity());
        return entity.getBoardId();
    }

    /**
     * 게시글 리스트 조회
     */
    public List<BoardResponseDto> findAll() {

        Sort sort = Sort.by(Direction.DESC, "boardId", "boardDate");
        List<Board> list = boardRepository.findAll(sort);
        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    
    /**
     * 게시글 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public Board detail(Long id) {
    	return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
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
