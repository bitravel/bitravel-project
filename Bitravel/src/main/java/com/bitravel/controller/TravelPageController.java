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

import com.bitravel.service.TravelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/travel")
@RequiredArgsConstructor
public class TravelPageController {
	
	private final TravelService travelService;
	
    /**
     * 여행지 전체 리스트 페이지
     */
    @GetMapping("/list")
    public String travel(Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
        
    	model.addAttribute("travelList", travelService.findAll(pageable));
    	
    	return "travel/list";
    }
    
    /**
     * 여행지 광역시별 리스트 페이지
     */
    @GetMapping("/list/{large}")
    public String travelLargeGov(@PathVariable String large, Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
    	
    	model.addAttribute("travelList", travelService.detailsByLargeGov(large, pageable));
    	model.addAttribute("largeGov", large);
    	return "travel/listLarge";
    }
    
    /**
     * 여행지 기초지자체별 리스트 페이지
     */
    @GetMapping("/list/{large}/{small}")
    public String travelSmallGov(@PathVariable String large, @PathVariable String small, Model model, @PageableDefault(size = 9, sort = "travelView", direction = Sort.Direction.DESC) Pageable pageable) {
    	
    	model.addAttribute("travelList", travelService.detailsBySmallGov(large, small, pageable));
    	model.addAttribute("largeGov", large);
    	model.addAttribute("smallGov", small);
    	return "travel/listSmall";
    }
    
    /**
     * 여행지 등록 페이지
     */
    @GetMapping("/write")
    public String openTravelWrite(@RequestParam(required = false) final Long id, Model model) {

    	model.addAttribute("id", id);
        return "travel/write";
    }
    
    /**
     * 여행지 상세 페이지
     */
    @GetMapping("/{id}")
    public String openDetailWriting(@PathVariable final Long id, Model model) {
    	String weatherKey = "k9ex5ipQp8k%2Baiet3GfC015PcRbjkuEv%2Bq8XD2ScEoT0CMfyyZgG5%2BjRCpsuFqQ2LFtwGcZdiDuigKZLvnn7yg%3D%3D";
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        model.addAttribute("weather", weatherKey);
        return "travel/detail";
    }

}
