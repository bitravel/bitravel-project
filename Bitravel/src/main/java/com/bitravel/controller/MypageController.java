package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.User;
import com.bitravel.service.MypageService;
import com.bitravel.service.UserService;

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
		UserDto user = userService.getMyUserWithAuthorities().get();
		model.addAttribute("user", user);
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

	
}
