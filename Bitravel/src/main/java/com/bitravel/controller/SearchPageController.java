package com.bitravel.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.data.dto.TravelSimpleDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.Travel;
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
    	List<TravelSimpleDto> tList = travelService.detailsByName(keyword);
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
    		@PageableDefault(size = 10, sort = "travelId", direction = Sort.Direction.ASC) Pageable travel) {
    	model.addAttribute("keyword", keyword);
    	long all = 0;
    	//model.addAttribute("travelList", travelService.findTravels(keyword, travel));
    	model.addAttribute("count", all);
    	return "/search/travel";
    }
    
    /**
     * 후기 검색 결과 전체 보기
     */
    
    /**
     * 게시물 검색 결과 전체 보기
     */
    
    /**
     * 회원 검색 결과 전체 보기
     */
}