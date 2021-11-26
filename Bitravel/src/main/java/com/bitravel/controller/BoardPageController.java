package com.bitravel.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardPageController {
	
	private final BoardService boardService;
	
    /**
     * 게시글 리스트 페이지
     */
    @GetMapping("/list")
    public String board(Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        
    	model.addAttribute("boardList", boardService.findAll(pageable));
    	
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
