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

import com.bitravel.data.dto.ReviewCommentRequestDto;
import com.bitravel.data.dto.ReviewCommentResponseDto;
import com.bitravel.service.ReviewCommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "ReviewCommentController")
public class ReviewCommentController {

    private final ReviewCommentService rCommentService;
    
    
    /**
     * 특정 게시글의 댓글 모두 보기
     */
    @GetMapping("/reviews/comments/{id}")
    @ApiOperation(value = "댓글 조회", notes = "parameter로 주어진 reviewId에 해당하는 댓글을 리스트로 모두 호출한다.")
    public List<ReviewCommentResponseDto> findAllComments(@PathVariable final Long id) {
        return rCommentService.findAllComments(id);
    }
    
    /**
     * 특정 게시물에 댓글 작성
     */
    @PostMapping("/reviews/comments/{id}")
    @ApiOperation(value = "댓글 작성", notes = "댓글 내용을 저장하는 API. ReviewComment entity 클래스로 데이터를 저장한다.")
    public Long save(@RequestBody final ReviewCommentRequestDto params) {    	
    	return rCommentService.saveComment(params);
    }
    
    /**
     * 특정 게시물에 댓글 수정 (review id은 hidden input tag 등으로 같이 전달되도록 해야 함)
     */
    @PatchMapping("/reviews/comments/{id}")
    @ApiOperation(value = "글 수정", notes = "댓글 내용을 수정하는 API. ReviewComment entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 cid 값을 함깨 보내줘야한다.")
    public Boolean save(@PathVariable final Long id, @RequestBody final ReviewCommentRequestDto params) {
        return rCommentService.update(id, params);
    }
    
    /**
     * 특정 게시물에 댓글 삭제
     */
    @DeleteMapping("/reviews/comments/{id}")
    @ApiOperation(value = "글 삭제", notes = "댓글 내용을 삭제하는 API. ReviewComment entity 클래스의 id 값으로 데이터를 삭제한다.")
    public Boolean deleteById(@PathVariable Long id) {	
    	return rCommentService.deleteById(id);
    }
    
}