package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewPageController {

    /**
     * 후기 리스트 페이지
     */
    @GetMapping("/list")
    public String openReviewList() {
        return "review/list";
    }

    /**
     * 후기 등록 페이지
     */
    @GetMapping("/write")
    public String openReviewWrite() {
        return "review/write";
    }

}
