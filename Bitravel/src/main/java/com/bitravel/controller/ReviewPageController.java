package com.bitravel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.ReviewTravels;
import com.bitravel.data.entity.Travel;
import com.bitravel.data.repository.ReviewRepository;
import com.bitravel.data.repository.TravelRepository;
import com.bitravel.service.ReviewService;
import com.bitravel.service.TravelService;
import com.bitravel.service.UserService;
import com.bitravel.util.SecurityUtil;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewPageController {
	
	private final ReviewService reviewService;
	private final ReviewRepository reviewRepository;
	private final TravelService travelService;
	private final TravelRepository travelRepository;
	private final UserService userService;
    /**
     * 후기 리스트 페이지
     */
    @GetMapping("")
    public String review (Model model, @PageableDefault(size = 5, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	//최신후기
    	model.addAttribute("reviewList", reviewService.findAll(pageable));
        
        //한달간 조회수 높은 후기
        model.addAttribute("viewList", reviewService.findViewAll(pageable));
        
        //나이대별 최신 후기
        Optional<UserDto> tmp = userService.getUserWithAuthorities(SecurityUtil.getCurrentEmail().get());
		if(tmp.isPresent()) {
			UserDto now = tmp.get();
			
			model.addAttribute("ageList", reviewService.findAgeAll(now.getAge(), pageable));
		}
    	return "review/reviewList";
    }
    
    /**
     * 후기 전체 리스트 페이지
     */
    @GetMapping("/")
    public String totalReview (Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("reviewList", reviewService.findAll(pageable));
    	return "review/reviewTotalList";
    }

    /**
     * 후기 등록 페이지
     */
    @GetMapping("/write")
    public String openReviewWrite(@RequestParam(required = false) final Long id, Model model) {
    	model.addAttribute("id", id);
    	return "review/reviewWrite";
    }
    
    /**
     * 후기 수정 페이지
     */
    @GetMapping("/modify")
    public String openReviewModify(@RequestParam(required = false) final Long id, Model model) {
    	model.addAttribute("id", id);
    	return "review/reviewModify";
    }
    
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/{id}")
    public String openDetailWriting(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        Review review = reviewRepository.getById(id);
        List<ReviewTravels> thisReview = reviewService.findByReview(review);
        List<Travel> list = new ArrayList<>();
        model.addAttribute("rtList", reviewService.findByReview(review));
        for(int i=0;i<thisReview.size();i++) {
			ReviewTravels now = thisReview.get(i);
			list.add(travelService.findByTravelId(now.getTravel().getTravelId()));
			model.addAttribute("tList", list);
        }
        return "review/reviewDetail";
    }
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/search/all/{id}")
    public String openDetailWritingOfAll(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        Review review = reviewRepository.getById(id);
        model.addAttribute("rtList", reviewService.findByReview(review));
        return "review/reviewDetail";
    }
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/search/nickname/{id}")
    public String openDetailWritingOfN(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        Review review = reviewRepository.getById(id);
        model.addAttribute("rtList", reviewService.findByReview(review));
        return "review/reviewDetail";
    }
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/search/title/{id}")
    public String openDetailWritingOfT(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        Review review = reviewRepository.getById(id);
        model.addAttribute("rtList", reviewService.findByReview(review));
        return "review/reviewDetail";
    }
    /**
     * 후기 상세 페이지
     */
    @GetMapping("/search/titleandcontent/{id}")
    public String openDetailWritingOfTC(@PathVariable final Long id, Model model, @RequestParam(value = "page") @Nullable final Long page) {
        model.addAttribute("id", id);  //model을 통해서 id값 넣어줌
        Review review = reviewRepository.getById(id);
        model.addAttribute("rtList", reviewService.findByReview(review));
        return "review/reviewDetail";
    }
    /**
     * 후기 통합 검색
     */
    @GetMapping("/search/all")
	@ApiOperation(value = "후기 통합 검색 목록", notes = "후기를 닉네임 또는 제목 또는 내용으로 조회하는 API.")
	public String findBoards(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("reviewList", reviewService.findReviews(keyword, pageable));
		return "review/reviewTotalList";
	}
    
    /**
	 * 후기 닉네임 검색
	 */
	@GetMapping("/search/nickname")
	@ApiOperation(value = "후기 닉네임 검색 목록", notes = "후기를 닉네임으로 조회하는 API.")
	public String findBoardsByNickname(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByNickname(keyword, pageable));
		return "review/reviewTotalList";
	}
	
	/**
	 * 후기 제목 검색 
	 */
	@GetMapping("/search/title")
	@ApiOperation(value = "후기 제목 검색 목록", notes = "후기를 제목으로 조회하는 API.")
	public String findBoardsByTitle(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByTitle(keyword, pageable));
		return "review/reviewTotalList";
	}
	
	/**
	 * 후기 제목+내용 검색 findBoardsByTitleAndContent
	 */
	@GetMapping("/search/titleandcontent")
	@ApiOperation(value = "후기 제목+내용 검색 목록", notes = "후기를 제목 또는 내용으로 조회하는 API.")
	public String findBoardsByTitleAndContent(@RequestParam(value = "keyword") String keyword, Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
    	model.addAttribute("boardList", reviewService.findReviewsByTitleAndContent(keyword, pageable));
		return "review/reviewTotalList";
	}


}
