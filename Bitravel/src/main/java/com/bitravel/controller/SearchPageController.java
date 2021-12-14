package com.bitravel.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.data.dto.TravelSimpleDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Board;
import com.bitravel.service.BoardService;
import com.bitravel.service.ReviewService;
import com.bitravel.service.TravelService;
import com.bitravel.service.UserService;
import com.bitravel.util.TagUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchPageController {
	
	private final TravelService travelService;
	private final ReviewService reviewService;
	private final BoardService boardService;
	private final UserService userService;
	
    /**
     * 검색 결과
     */
    @GetMapping("/search")
    public String openSearchPage(Model model, @RequestParam(value = "keyword") String keyword) {
    	model.addAttribute("keyword", keyword);
    	long all = 0;
    	List<TravelSimpleDto> tList = travelService.findByName(keyword);
    	List<Board> bList = boardService.findBoards(keyword);
    	List<UserDto> uList = userService.getUserListBynickname(keyword);
    	
    	// 관리자 정보 숨기기
    	for(int i=0;i<uList.size();i++) {
    		if(uList.get(i).getUserId() == 1) {
    			uList.remove(i);
    		}
    	}
    	
    	all += tList.size();
    	all += bList.size();
    	all += uList.size();
    	
    	model.addAttribute("count", all);
    	model.addAttribute("tcount", tList.size());
    	model.addAttribute("bcount", bList.size());
    	model.addAttribute("ucount", uList.size());
    	
    	if(tList.size()>10)
    		tList = tList.subList(0, 10);
    	if(bList.size()>10)
    		bList = bList.subList(0, 10);
    	if(uList.size()>10)
    		uList = uList.subList(0, 10);
    	
    	for(int i=0;i<bList.size();i++) {
    		Board now = bList.get(i);
    		now.setBoardContent(TagUtil.getText(now.getBoardContent()));
    	}
    	
    	model.addAttribute("travelList", tList);
    	model.addAttribute("boardList", bList);
    	model.addAttribute("userList", uList);
    	return "/search/list";
    }
    
    /**
     * 여행지 검색 결과 전체 보기
     */
    @GetMapping("/search/travel")
    public String openSearchTravel(Model model, @RequestParam(value = "keyword") String keyword,
    		@PageableDefault(size = 9, sort = "travelName", direction = Sort.Direction.ASC) Pageable travel) {
    	model.addAttribute("keyword", keyword);
    	List<TravelSimpleDto> tList = travelService.findByName(keyword);
    	long all = tList.size();
    	model.addAttribute("travelList", travelService.findByName(keyword, travel));
    	model.addAttribute("count", all);
    	return "/search/travel";
    }
    
    /**
     * 후기 검색 결과 전체 보기
     */
    
    /**
     * 게시물 검색 결과 전체 보기
     */
    @GetMapping("/search/board")
    public String openSearchBoard(Model model, @RequestParam(value = "keyword") String keyword,
    		@PageableDefault(size = 9, sort = "boardId", direction = Sort.Direction.DESC) Pageable board) {
    	model.addAttribute("keyword", keyword);
    	List<Board> bList = boardService.findBoards(keyword);
    	long all = bList.size();
    	
    	for(int i=0;i<bList.size();i++) {
    		Board now = bList.get(i);
    		now.setBoardContent(TagUtil.getText(now.getBoardContent()));
    	}
    	
    	// 리스트 편집한 결과를 토대로 Page 새로 만들기
    	Sort sort = Sort.by(Sort.Direction.DESC, "boardView").and(Sort.by(Direction.DESC, "boardDate"));
    	int page = (board.getPageNumber() == 0) ? 0 : (board.getPageNumber() - 1);
    	board = PageRequest.of(page, 15, sort);
    	Page<Board> bpage = new PageImpl<>(bList.subList((int) board.getOffset(), (int) Math.min(board.getOffset() + board.getPageSize(), all)), board, all);
    	
    	
    	model.addAttribute("boardList", bpage);
    	model.addAttribute("count", all);
    	return "/search/board";
    }
    
    /**
     * 회원 검색 결과 전체 보기
     */
    @GetMapping("/search/user")
    public String openSearchUser(Model model, @RequestParam(value = "keyword") String keyword,
    		@PageableDefault(size = 9, sort = "userDate", direction = Sort.Direction.ASC) Pageable user) {
    	model.addAttribute("keyword", keyword);
    	List<UserDto> uList = userService.getUserListBynickname(keyword);
    	
    	// 관리자 정보 숨기기
    	for(int i=0;i<uList.size();i++) {
    		if(uList.get(i).getUserId() == 1) {
    			uList.remove(i);
    		}
    	}
    	
    	long all = uList.size();
    	
    	// 리스트 편집한 결과를 토대로 Page 새로 만들기
    	Sort sort = Sort.by(Sort.Direction.DESC, "point").and(Sort.by(Direction.ASC, "userDate"));
    	int page = (user.getPageNumber() == 0) ? 0 : (user.getPageNumber() - 1);
    	user = PageRequest.of(page, 15, sort);
    	Page<UserDto> upage = new PageImpl<>(uList.subList(((int) user.getOffset()), (int) Math.min(user.getOffset() + user.getPageSize(), all)), user, all);
    	
    	model.addAttribute("userList", upage);
    	model.addAttribute("count", all);
    	return "/search/user";
    }
}