package com.bitravel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@PostMapping("/updatePassword")
	public String updatePassword(@ModelAttribute("user") UserDto userDto, Model model) {
		UserDto user = userService.getMyUserWithAuthorities().get();
		String pw = user.getPassword();
//		User user = mypageService.updatePassword(userDto);
//		model.addAttribute("user", user);
		return "redirect:/mypage/setting";
	}
	
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
