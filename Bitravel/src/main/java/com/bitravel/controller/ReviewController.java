package com.bitravel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.dto.ReviewRequestDto;
import com.bitravel.data.dto.ReviewResponseDto;
import com.bitravel.service.ReviewService;
import com.bitravel.util.SecurityUtil;
import com.bitravel.util.TagUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(value = "ReviewController")
public class ReviewController {

	// 단순 게시판에 비하여 여행지 등록 및 불러오기 관련 기능이 추후 추가될 수 있음
	private final ReviewService reviewService;

	/**
	 * 후기 작성
	 */
	@PostMapping("/reviews")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@ApiOperation(value = "후기 작성", notes = "후기 내용을 저장하는 API. Review entity 클래스로 데이터를 저장한다.")
	public Long save(@RequestBody final ReviewRequestDto params) {
		
    	String newTitle = TagUtil.getText(params.getReviewTitle());
    	if(newTitle.isBlank())
    		throw new RuntimeException("너무 제목이 짧습니다.");
    	params.setReviewTitle(newTitle);
		
		return reviewService.save(params);
	}

	/**
	 * 후기 수정
	 */
	@PatchMapping("/reviews/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@ApiOperation(value = "후기 수정", notes = "후기 내용을 수정하는 API. Review entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 bid 값을 함깨 보내줘야한다.")
	public Boolean update(@PathVariable final Long id, @RequestBody final ReviewRequestDto params) {
		
    	String newTitle = TagUtil.getText(params.getReviewTitle());
    	if(newTitle.isBlank())
    		throw new RuntimeException("너무 제목이 짧습니다.");
    	params.setReviewTitle(newTitle);
		
		return reviewService.update(id, params);
	}
	/**
	 * 후기 상세 정보 조회
	 */
	@GetMapping("/reviews/{id}")
	@ApiOperation(value = "후기 내용 조회", notes = "개별 후기의 정보를 조회하는 API. Review entity 클래스의 bid값을 기준으로 데이터를 가져온다.")
	public ReviewResponseDto detail(@PathVariable final Long id) {
		return reviewService.detail(id);
	}
	/**
	 * 후기 삭제
	 */
	@DeleteMapping("/reviews/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@ApiOperation(value = "후기 삭제", notes = "후기 내용을 삭제하는 API. Review entity 클래스의 bid 값으로 데이터를 삭제한다.")
	public Boolean deleteById(@PathVariable Long id) {	
		return reviewService.deleteById(id);
	}
	
	/**
     * 후기 수정/삭제 가능 여부 조회
     */
    @GetMapping("/reviews/modify/{id}")
    @ApiOperation(value = "글 내용 조회", notes = "개별 글의 정보를 조회하는 API. Board entity 클래스의 bid값을 기준으로 데이터를 가져온다.")
    public ResponseEntity<?> checkWriter(@PathVariable final Long id) {    	
    	if(reviewService.detail(id).getUserEmail().equals(SecurityUtil.getCurrentEmail().get())) {
    		return ResponseEntity.ok(null);
    	} else if(SecurityUtil.getCurrentEmail().get().equals("admin")) {
    		return ResponseEntity.ok(null);
    	} else {
    		return ResponseEntity.status(401).body(null);
    	}
    }

}