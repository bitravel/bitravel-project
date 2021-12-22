package com.bitravel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.data.dto.PasswordDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.dto.UserUpdateDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.Review;
import com.bitravel.data.entity.User;
import com.bitravel.service.MypageService;
import com.bitravel.service.UserService;
import com.bitravel.util.SecurityUtil;
import com.bitravel.util.TagUtil;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage")
@Api(value = "MypageController")
public class MypageController {

	private final UserService userService;
	private final MypageService mypageService;
	
	@GetMapping("")
	public String myPage(Model model) {
		// 개인정보 불러오기
		UserDto user = userService.getMyUserWithAuthorities().get();
		model.addAttribute("user", user);
		// 내가 작성한 리뷰 불러오기
		List<Review> reviewList = mypageService.getMyReview(user.getEmail());
		model.addAttribute("rcount", reviewList.size());
		model.addAttribute("reviewList", reviewList);
		// 내가 작성한 게시글 불러오기
		List<Board> boardList = mypageService.getMyBoard(user.getEmail());
		model.addAttribute("bcount", boardList.size());
		for(int i=0;i<boardList.size();i++) {
			boardList.get(i).setBoardContent(TagUtil.getText(boardList.get(i).getBoardContent()));
		}
		model.addAttribute("boardList", boardList);
		return "user/mypage";
	}
	
	@GetMapping("/setting")
	public String myPageSetting(Model model) {
		UserDto user = userService.getMyUserWithAuthorities().get();
		model.addAttribute("user", user);
		return "user/mypageSetting";
	}
	
	@PostMapping("/update")
	public String updateMyPage(@RequestBody UserUpdateDto userDto, Model model) {
		User user = mypageService.updateUser(userDto);
		model.addAttribute("user", user);
		return "redirect:/mypage/setting";
	}
	
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody PasswordDto params, Model model) {
	
		Integer validation = userService.isValidPassword(params);
		
		if(validation==-1) {
			return ResponseEntity.status(401).body(null);
		} else if(validation==0) {
			return ResponseEntity.status(400).body(null);
		}
		
		if(userService.updateUserPassword(SecurityUtil.getCurrentEmail().get(), params.getNewPassword()))
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.internalServerError().body(null);
	}	
}
