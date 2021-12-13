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

import com.bitravel.service.BoardService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardPageController {
	
	private final BoardService boardService;
	
    /**
     * 게시글 리스트 페이지
     */
    @GetMapping("")
    public String board(Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        
    	model.addAttribute("boardList", boardService.findAll(pageable));
    	
    	return "redirect:board/list";
    }
    
    @GetMapping("/list")
    public String boardList(Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        
    	model.addAttribute("boardList", boardService.findAll(pageable));
    	
    	return "board/list";
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
    @GetMapping("/{id}")
    public String openWriting(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "board/detail";
    }
   
    
    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/search/all/{id}")
    public String openDetailWritingOfAll(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "board/detail";
    }
    
    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/search/nickname/{id}")
    public String openDetailWritingOfN(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "board/detail";
    }
    
    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/search/title/{id}")
    public String openDetailWritingOfT(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "board/detail";
    }
    
    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/search/titleandcontent/{id}")
    public String openDetailWritingOfTC(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        
        return "board/detail";
    }
    
    /**
     * 게시글 통합 검색
     */
    @GetMapping("/search/all/list")
	@ApiOperation(value = "게시글 통합 검색 목록", notes = "게시글을 닉네임 또는 제목 또는 내용으로 조회하는 API.")
	public String findBoards(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", boardService.findBoards(keyword, pageable));
		return "board/list";
	}
    /**
	 * 게시글 닉네임 검색
	 */
	@GetMapping("/search/nickname/list")
	@ApiOperation(value = "게시글 닉네임 검색 목록", notes = "게시글을 닉네임으로 조회하는 API.")
	public String findBoardsByNickname(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", boardService.findBoardsByNickname(keyword, pageable));
		return "board/list";
	}
	
	/**
	 * 게시글 제목 검색 
	 */
	@GetMapping("/search/title/list")
	@ApiOperation(value = "게시글 제목 검색 목록", notes = "게시글을 제목으로 조회하는 API.")
	public String findBoardsByTitle(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", boardService.findBoardsByTitle(keyword, pageable));
		return "board/list";
	}
	
	/**
	 * 게시글 제목+내용 검색 findBoardsByTitleAndContent
	 */
	@GetMapping("/search/titleandcontent/list")
	@ApiOperation(value = "게시글 제목+내용 검색 목록", notes = "게시글을 제목 또는 내용으로 조회하는 API.")
	public String findBoardsByTitleAndContent(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", boardService.findBoardsByTitleAndContent(keyword, pageable));
		return "board/list";
	}

}
