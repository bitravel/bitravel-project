package com.bitravel.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.service.BoardService;
import com.bitravel.service.ReviewService;

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
     * 후기 등록 페이지
     */
    @GetMapping("/write")
    public String openReviewWrite() {
        return "review/reviewWrite";
    }

}
