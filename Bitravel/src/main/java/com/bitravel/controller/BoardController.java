package com.bitravel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.dto.BoardRequestDto;
import com.bitravel.data.dto.BoardResponseDto;
import com.bitravel.data.entity.Board;
import com.bitravel.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "BoardController")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     */
    @PostMapping("/boards")
    @ApiOperation(value = "글 작성", notes = "글 내용을 저장하는 API. Board entity 클래스로 데이터를 저장한다.")
    public Long save(@RequestBody final BoardRequestDto params) {
        return boardService.save(params);
    }

    /**
     * 게시글 리스트 조회
     */
    @GetMapping("/boards")
    @ApiOperation(value = "글 목록 조회", notes = "글 목록을 조회하는 API.")
    public List<BoardResponseDto> findAll() {
        return boardService.findAll();
    }
    
    /**
	 * 게시글 통합 검색
	 */
	@GetMapping("/boards/search/all")
	@ApiOperation(value = "게시글 통합 검색 목록", notes = "게시글을 닉네임 또는 제목 또는 내용으로 조회하는 API.")
	public List<BoardResponseDto> findBoards(String keyword) {
		return boardService.findBoards(keyword);
	}

	/**
	 * 게시글 닉네임 검색
	 */
	@GetMapping("/boards/search/nickname")
	@ApiOperation(value = "게시글 닉네임 검색 목록", notes = "게시글을 닉네임으로 조회하는 API.")
	public List<BoardResponseDto> findBoardsByNickname(String keyword) {
		return boardService.findBoardsByNickname(keyword);
	}
	
	/**
	 * 게시글 제목 검색
	 */
	@GetMapping("/boards/search/title")
	@ApiOperation(value = "게시글 제목 검색 목록", notes = "게시글을 제목으로 조회하는 API.")
	public List<BoardResponseDto> findBoardsByTitle(String keyword) {
		return boardService.findBoardsByTitle(keyword);
	}
	
	/**
	 * 게시글 제목+내용 검색
	 */
	@GetMapping("/boards/search/titleandcontent")
	@ApiOperation(value = "게시글 제목+내용 검색 목록", notes = "게시글을 제목 또는 내용으로 조회하는 API.")
	public List<BoardResponseDto> findBoardsByTitleAndContent(String keyword) {
		return boardService.findBoardsByTitleAndContent(keyword);
	}
    
    /**
     * 게시글 수정
     */
    @PatchMapping("/boards/{id}")
    @ApiOperation(value = "글 수정", notes = "글 내용을 수정하는 API. Board entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 bid 값을 함깨 보내줘야한다.")
    public Boolean save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
        return boardService.update(id, params);
    }
    /**
     * 게시글 상세 정보 조회
     */
    @GetMapping("/boards/{id}")
    @ApiOperation(value = "글 내용 조회", notes = "개별 글의 정보를 조회하는 API. Board entity 클래스의 bid값을 기준으로 데이터를 가져온다.")
    public BoardResponseDto detail(@PathVariable final Long id) {
    	return boardService.detail(id);
    }
    /**
     * 게시글 삭제
     */
    @DeleteMapping("/boards/{id}")
    @ApiOperation(value = "글 삭제", notes = "글 내용을 삭제하는 API. Board entity 클래스의 bid 값으로 데이터를 삭제한다.")
    public Boolean deleteById(@PathVariable Long id) {	
    	return boardService.deleteById(id);
    }
    
}