package com.bitravel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.Board;
import com.bitravel.data.entity.Review;
import com.bitravel.service.BoardService;
import com.bitravel.service.ReviewService;
import com.bitravel.service.UserService;
import com.bitravel.util.SecurityUtil;
import com.bitravel.util.TagUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserPageController {

	private final UserService userService;
	private final ReviewService reviewService;
	private final BoardService boardService;

	@GetMapping("/login")
	@PreAuthorize("isAnonymous()")
	public String openLoginPage() {
		return "user/login";
	}

	@GetMapping("/signup")
	@PreAuthorize("isAnonymous()")
	public String openFirstSignUpPage() {
		return "user/signUp";
	}

	@GetMapping("/signup/second")
	@PreAuthorize("isAnonymous()")
	public String openSecondSignUpPage(@RequestParam("userEmail") String email, Model model) {
		model.addAttribute("userEmail", email);
		return "user/signUp2";
	}

	@GetMapping("/signup/third")
	@PreAuthorize("isAnonymous()")
	public String openThirdSignUpPage(@RequestParam("userEmail") String email, Model model) {
		model.addAttribute("userEmail", email);
		return "user/signUp3";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public String openAdminPage() {
		return "user/admin";
	}

	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public String openUserPage(@RequestParam String email, Model model) {
		try {
			// 개인정보 불러오기
			Optional<UserDto> tmp = userService.getUserWithAuthorities(email);
			model.addAttribute("user", tmp.get());

			// 내가 작성한 리뷰 불러오기
			List<Review> reviewList = reviewService.findReviewsByEmail(email);
			model.addAttribute("rcount", reviewList.size());
			model.addAttribute("reviewList", reviewList);

			// 내가 작성한 게시글 불러오기
			List<Board> boardList = boardService.findBoardsByEmail(email);
			model.addAttribute("bcount", boardList.size());
			for (int i = 0; i < boardList.size(); i++) {
				boardList.get(i).setBoardContent(TagUtil.getText(boardList.get(i).getBoardContent()));
			}
			model.addAttribute("boardList", boardList);
			
			if(email.equals("admin"))
				return "redirect:/admin";
			else if (email.equals("anonymousUser") || tmp.isEmpty()
					|| SecurityUtil.getCurrentEmail().get().equals(email))
				return "redirect:/mypage";
			else
				return "user/userPage";
		} catch (Exception e) {
			return "redirect:/mypage";
		}
	}

}
