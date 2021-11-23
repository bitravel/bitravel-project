package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String openBoardWrite(@RequestParam(required = false) final Long id, Model model) {
        model.addAttribute("id", id);
        return "board/write";
    }
    
    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/detail/{id}")
    public String openDetailWriting(@PathVariable final Long id, Model model) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        return "board/detail";
    }

}
