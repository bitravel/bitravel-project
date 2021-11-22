package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardPageController {

    /**
     * 게시글 리스트 페이지
     */
    @GetMapping("/list")
    public String openBoardList() {
        return "board/board";
    }

    /**
     * 게시글 등록 페이지
     */
    @GetMapping("/write")
    public String openBoardWrite() {
        return "board/write";
    }

}
