package com.bitravel.service;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.dto.BoardRequestDto;
import com.bitravel.data.dto.BoardResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.User;
import com.bitravel.data.repository.BoardCommentRepository;
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
	private final BoardCommentRepository bCommentRepository;
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
    	} else if(nowUserEmail.equals("admin")) {
    		params.setNickname(userRepository.findOneWithAuthoritiesByEmailAndActivated(nowUserEmail, true).get().getNickname());
    	} else {
    		User entity = userRepository.findOneWithAuthoritiesByEmailAndActivated(nowUserEmail, true).get();
    		entity.changePoint(20);
    		params.setNickname(entity.getNickname());
    	}
        Board entity = boardRepository.save(params.toEntity());
        return entity.getBoardId();
    }

    /**
     * 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
    	return boardRepository.findAll(pageable);
    }
    
    /**
     * 인기 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    public Page<Board> findBestList(Pageable pageable) {
    	Date start = new Date(System.currentTimeMillis()-86400000L*30); // 현재 3일 기준
    	Date end = new Date();
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardView", "boardDate"));
    	// 해당 기간 중 조회수 10 이상인 글 반환
    	return boardRepository.findByBoardDateBetweenAndBoardViewGreaterThan(start, end, pageable, 10);
    }
    
    /**
     * 게시글 통합 검색 결과 조회
     */
    @Transactional
    public Page<Board> findBoards(String keyword, Pageable pageable) {
    	Sort sort = Sort.by(Sort.Direction.DESC, "boardId");
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, sort);
    	
        return boardRepository.findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(keyword, keyword, keyword, pageable);
    }
    
    /**
     * 게시글 통합 검색 결과 조회 (Pageable X)
     */
    @Transactional
    public List<Board> findBoards(String keyword) { 	
    	Sort sort = Sort.by(Sort.Direction.DESC, "boardView", "boardDate");
        return boardRepository.findByNicknameContainingOrBoardTitleContainingOrBoardContentContaining(keyword, keyword, keyword, sort);
    }
    
    /**
     * 게시글 닉네임 검색 결과 조회
     */
    @Transactional
    public Page<Board> findBoardsByNickname(String keyword, Pageable pageable) {
    	
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
    	
        return boardRepository.findByNicknameContaining(keyword, pageable);
    }
    
    /**
     * 게시글 제목 검색 결과 조회
     */
    @Transactional
    public Page<Board> findBoardsByTitle(String keyword, Pageable pageable) {
    	
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
    	
        return boardRepository.findByBoardTitleContaining(keyword, pageable);
    }
    
    /**
     * 게시글 제목+내용 검색 결과 조회
     */
    @Transactional
    public Page<Board> findBoardsByTitleAndContent(String keyword, Pageable pageable) {
    	
    	int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
    	
        return boardRepository.findByBoardTitleContainingOrBoardContentContaining(keyword, keyword, pageable);
    }
    
    /**
     * 게시글 상세 정보 조회
     */
    @Transactional
    public BoardResponseDto findById(Long id) {
    	Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    	entity.increaseView();
    	return new BoardResponseDto(entity);
    }
    
    /**
     * 게시글 상세 정보 조회 (조회수 증가 X)
     */
    @Transactional
    public BoardResponseDto findByIdNoViewCount(Long id) {
    	Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
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
        String myEmail = SecurityUtil.getCurrentEmail().get();
        if(myEmail.equals("admin")) {
        	log.info("관리자 권한으로 글을 삭제합니다. 글 번호 : "+id);
        } else if(!entity.getUserEmail().equals(myEmail)) {
        	log.info("유효하지 않은 삭제 요청입니다.");
        	return false;
        } else {
        	User user = userRepository.findOneWithAuthoritiesByEmailAndActivated(myEmail, true).get();
            user.changePoint(-20);
        }    
        // 해당 글의 댓글도 같이 삭제해야 함
        bCommentRepository.deleteAllByBoard(entity);
    	boardRepository.deleteById(id);
    	return true;
    }
    
}
