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

import com.bitravel.data.dto.BoardCommentRequestDto;
import com.bitravel.data.dto.BoardCommentResponseDto;
import com.bitravel.service.BoardCommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "BoardCommentController")
public class BoardCommentController {

    private final BoardCommentService bCommentService;
    
    
    /**
     * 특정 게시글의 댓글 모두 보기
     */
    @GetMapping("/comments/{id}")
    @ApiOperation(value = "댓글 조회", notes = "parameter로 주어진 boardId에 해당하는 댓글을 리스트로 모두 호출한다.")
    public List<BoardCommentResponseDto> findAllComments(@PathVariable final Long id) {
        return bCommentService.findAllComments(id);
    }
    
    /**
     * 특정 게시물에 댓글 작성
     */
    @PostMapping("/comments/{id}")
    @ApiOperation(value = "댓글 작성", notes = "댓글 내용을 저장하는 API. BoardComment entity 클래스로 데이터를 저장한다.")
    public Long save(@RequestBody final BoardCommentRequestDto params) {    	
    	return bCommentService.saveComment(params);
    }
    
    /**
     * 특정 게시물에 댓글 수정 (board id은 hidden input tag 등으로 같이 전달되도록 해야 함)
     */
    @PatchMapping("/comments/{id}")
    @ApiOperation(value = "글 수정", notes = "댓글 내용을 수정하는 API. BoardComment entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 cid 값을 함깨 보내줘야한다.")
    public Boolean save(@PathVariable final Long id, @RequestBody final BoardCommentRequestDto params) {
        return bCommentService.update(id, params);
    }
    
    /**
     * 특정 게시물에 댓글 삭제
     */
    @DeleteMapping("/comments/{id}")
    @ApiOperation(value = "글 삭제", notes = "댓글 내용을 삭제하는 API. BoardComment entity 클래스의 id 값으로 데이터를 삭제한다.")
    public Boolean deleteById(@PathVariable Long id) {	
    	return bCommentService.deleteById(id);
    }
    
}