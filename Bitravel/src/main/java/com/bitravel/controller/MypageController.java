package com.bitravel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.User;
import com.bitravel.service.MypageService;
import com.bitravel.service.ReviewService;
import com.bitravel.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage")
@Api(value = "MypageController")
public class MypageController {

	private final UserService userService;
	private final ReviewService reviewService;
	private final MypageService mypageService;
	
	@GetMapping("")
	public String myPage(Model model) {
		// 개인정보 불러오기
		UserDto user = userService.getMyUserWithAuthorities().get();
		model.addAttribute("user", user);
		// 내가 작성한 리뷰 불러오기
		List<Review> reviewList = mypageService.getMyReview(user.getEmail());
		model.addAttribute("reviewList", reviewList);
		return "user/mypage";
	}
	
	@GetMapping("/setting")
	public String myPageSetting(Model model) {
		UserDto user = userService.getMyUserWithAuthorities().get();
		model.addAttribute("user", user);
		return "user/mypageSetting";
	}
	
	@PostMapping("/update")
	public String updateMyPage(@ModelAttribute("user") UserDto userDto, Model model) {
		User user = mypageService.updateUser(userDto);
		model.addAttribute("user", user);
		return "redirect:/mypage/setting";
	}
	@PostMapping("/updatePassword")
	public String updatePassword(@ModelAttribute("user") UserDto userDto, Model model) {
		UserDto user = userService.getMyUserWithAuthorities().get();
		String pw = user.getPassword();
//		User user = mypageService.updatePassword(userDto);
//		model.addAttribute("user", user);
		return "redirect:/mypage/setting";
	}
	 
	/**
     * 나의 후기 전체 리스트 페이지
     */
//    @GetMapping("/")
//    public String totalMyReview (Model model, @PageableDefault(size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
//        model.addAttribute("reviewList", reviewService.findAll(pageable));
//    	return "review/reviewTotalList";
//    }

	
//	@PostMapping("/user/modifyPassword")
//	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//	public ResponseEntity<Boolean> modifyUserPassword (String email, String password) {
//		return ResponseEntity.ok(userService.updateUserPassword(email, password));
//	}
//
//	@GetMapping("user/delete")
//	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//	public ResponseEntity<Boolean> deleteUser(String email) {
//		return ResponseEntity.ok(userService.deleteUser(email));
//	}
//	
//	@PostMapping("user/delete")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//	public ResponseEntity<Boolean> deleteUserByList(@RequestBody List<Long> list) {
//		return ResponseEntity.ok(userService.deleteUserByList(list));
//	}

	
}
