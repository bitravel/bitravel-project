package com.bitravel.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.data.dto.TravelSimpleDto;
import com.bitravel.data.entity.Travel;
import com.bitravel.service.ReviewService;
import com.bitravel.service.TravelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/travel")
@RequiredArgsConstructor
public class TravelPageController {
	
	private final TravelService travelService;
	private final ReviewService reviewService;
	
    /**
     * 여행지 전체 리스트 페이지
     */
    @GetMapping("")
    public String travel(Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
        
    	model.addAttribute("travelList", travelService.findAll(pageable));
    	
    	return "travel/travelList";
    }
    
    /**
     * 여행지 광역시별 리스트 페이지
     */
    @GetMapping("/list/{large}")
    public String travelLargeGov(@PathVariable String large, Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
    	
    	model.addAttribute("travelList", travelService.findByLargeGov(large, pageable));
    	model.addAttribute("largeGov", large);
    	return "travel/travelListLarge";
    }
    
    /**
     * 여행지 기초지자체별 리스트 페이지
     */
    @GetMapping("/list/{large}/{small}")
    public String travelSmallGov(@PathVariable String large, @PathVariable String small, Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
    	
    	model.addAttribute("travelList", travelService.findBySmallGov(large, small, pageable));
    	model.addAttribute("largeGov", large);
    	model.addAttribute("smallGov", small);
    	return "travel/travelListSmall";
    }
    
    /**
     * 여행지 등록 페이지
     */
    @GetMapping("/write")
    public String openTravelWrite(@RequestParam(required = false) final Long id, Model model) {

    	model.addAttribute("id", id);
        return "travel/travelWrite";
    }
    
    /**
     * 여행지 상세 페이지
     */
    @GetMapping("/{id}")
    public String openDetailWriting(@PathVariable final Long id, Model model) {
    	
    	Travel now = travelService.findByIdNoViewCount(id);
    	String largeGov = now.getLargeGov();
    	String smallGov = now.getSmallGov();
    	List<TravelSimpleDto> tlist = null;
    	if(largeGov.equals("서울")||largeGov.equals("부산")&&!smallGov.equals("기장군")||largeGov.equals("대구")&&!smallGov.equals("달성군")
    		||largeGov.equals("인천")&&(!smallGov.equals("강화군")||!smallGov.equals("옹진군"))||largeGov.equals("대전")||largeGov.equals("광주")
    		||largeGov.equals("울산")&&!smallGov.equals("울주군")) {
    		tlist = travelService.findNotVisitedTravelByLargeGov(largeGov, id);
    		} else {
    			tlist = travelService.findNotVisitedTravelBySmallGov(largeGov, smallGov, id);
    		}
    	
    	model.addAttribute("travelList", tlist);
    	model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
    	model.addAttribute("name", now.getTravelName());
    	
    	model.addAttribute("reviewList", reviewService.find20ByTravel(id));
        
        return "travel/travelDetail";
    }

}
