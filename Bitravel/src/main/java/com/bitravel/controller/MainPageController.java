package com.bitravel.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.Travel;
import com.bitravel.service.ReviewService;
import com.bitravel.service.TravelService;
import com.bitravel.service.UserService;
import com.bitravel.util.SecurityUtil;
import com.bitravel.util.TagUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainPageController {

	private final TravelService travelService;
	private final ReviewService reviewService;
	private final UserService userService;

	@Transactional
	@GetMapping("")
	public String openIndexPage(Model model, @PageableDefault(size = 30, sort = "travelView", direction = Sort.Direction.DESC) Pageable travel) {
		
		// 첫번째
		model.addAttribute("List1", travelService.findAll(travel));
		
		// 두번째
		Optional<UserDto> tmp = userService.getUserWithAuthorities(SecurityUtil.getCurrentEmail().get());
		if(tmp.isPresent()) {
			UserDto now = tmp.get();
			
			List<Travel> simList = travelService.findSimiliarAgeGenderTaste(now.getAge(), now.getGender());
			model.addAttribute("favR", simList.get(0).getLargeGov());
			// 리스트 편집한 결과를 토대로 Page 새로 만들기
			Collections.sort(simList, new Comparator<Travel>() {
				@Override
				public int compare(Travel o1, Travel o2) {
					if(o1.getTravelView()==o2.getTravelView()) {
						return o1.getTravelName().compareTo(o2.getTravelName());
					} else {
						return o2.getTravelView()-o1.getTravelView();
					}
				} 		
			});
			Page<Travel> tpage = new PageImpl<>(simList.subList((int) travel.getOffset(), (int) Math.min(travel.getOffset() + travel.getPageSize(), simList.size())), travel, simList.size());
			model.addAttribute("List2", tpage);
		}

		// 세번째
		Page<Travel> favC = travelService.findFavoriteCategoryList(travel);
		List<Travel> favCList = favC.getContent();
		String middleC = "";
		String smallC = "";
		for(int i=0;i<favCList.size();i++) {
			if (favCList.get(i).getMiddleCategory()!=null) {
				middleC = favCList.get(i).getMiddleCategory();
				smallC = favCList.get(i).getSmallCategory();
				break;
			}
		}    	
		model.addAttribute("middleC", middleC);
		model.addAttribute("smallC", smallC);
		model.addAttribute("List3", favC);
		
		try {
		// 네번째
		model.addAttribute("List4", travelService.findUsersLike());
		} catch(Exception e) {
			log.info(e+"*##################");
		}
		
		// 다섯번째
		List<Review> favlist = reviewService.findAllForMe();
		for(int i=0;i<favlist.size();i++) {
			Review now = favlist.get(i);
			now.setReviewContent(TagUtil.getText(now.getReviewContent()));
		}
		model.addAttribute("List5", favlist);
		
		// 여섯번째
		List<Review> anolist = reviewService.findAllForMain();
		for(int i=0;i<anolist.size();i++) {
			Review now = anolist.get(i);
			now.setReviewContent(TagUtil.getText(now.getReviewContent()));
		}
		model.addAttribute("rcount", favlist.size());
		model.addAttribute("List6", anolist);

		return "index";
	}
}