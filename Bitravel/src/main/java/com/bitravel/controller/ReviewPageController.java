package com.bitravel.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.service.ReviewService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewPageController {
	
	private final ReviewService reviewService;

    /**
     * 후기 리스트 페이지
     */
    @GetMapping("/list")
    public String review (Model model, @PageableDefault(size = 5, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("reviewList", reviewService.findAll(pageable));
    	return "review/reviewList";
    }
    
    /**
     * 후기 전체 리스트 페이지
     */
    @GetMapping("/total/list")
    public String totalReview (Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("reviewList", reviewService.findAll(pageable));
    	return "review/reviewTotalList";
    }

    /**
     * 후기 등록 페이지
     */
    @GetMapping("/write")
    public String openReviewWrite(@RequestParam(required = false) final Long id, Model model) {
    	model.addAttribute("id", id);
    	return "review/reviewWrite";
    }
    
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/detail/{id}")
    public String openDetailWriting(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "review/reviewDetail";
    }
    /**
     * 후기 통합 검색
     */
    @GetMapping("/search/all/list")
	@ApiOperation(value = "후기 통합 검색 목록", notes = "후기를 닉네임 또는 제목 또는 내용으로 조회하는 API.")
	public String findBoards(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("reviewList", reviewService.findReviews(keyword, pageable));
		return "review/reviewList";
	}
    
    /**
	 * 후기 닉네임 검색
	 */
	@GetMapping("/search/nickname/list")
	@ApiOperation(value = "후기 닉네임 검색 목록", notes = "후기를 닉네임으로 조회하는 API.")
	public String findBoardsByNickname(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByNickname(keyword, pageable));
		return "review/reviewList";
	}
	
	/**
	 * 후기 제목 검색 
	 */
	@GetMapping("/search/title/list")
	@ApiOperation(value = "후기 제목 검색 목록", notes = "후기를 제목으로 조회하는 API.")
	public String findBoardsByTitle(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByTitle(keyword, pageable));
		return "review/reviewList";
	}
	
	/**
	 * 후기 제목+내용 검색 findBoardsByTitleAndContent
	 */
	@GetMapping("/search/titleandcontent/list")
	@ApiOperation(value = "후기 제목+내용 검색 목록", notes = "후기를 제목 또는 내용으로 조회하는 API.")
	public String findBoardsByTitleAndContent(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByTitleAndContent(keyword, pageable));
		return "review/reviewList";
	}


}
