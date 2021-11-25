package com.bitravel.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.TokenDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.entity.User;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.service.UserService;
import com.bitravel.util.ScriptUtil;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
@Api(value = "UserController")
public class UserController {

	private final UserService userService;

	@PostMapping("/login")
	public String login(@Valid LoginDto loginDto, HttpServletResponse response) throws IOException {
		try {
			TokenDto tokenInfo = userService.getUserAuthentication(loginDto);
			if(tokenInfo==null) {
				ScriptUtil.alertAndBackPage(response, "비밀번호를 다시 입력해 주세요.");
				return null;
			}
			String jwt = tokenInfo.getToken();
			response.setHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
		} catch (Exception e) {
			ScriptUtil.alertAndBackPage(response, "아이디를 찾을 수 없습니다.");
			return null;
		}
		return "index";
	}

	@PostMapping("/logout")
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		String jwt = response.getHeader(JwtFilter.AUTHORIZATION_HEADER);
		log.info("token: "+jwt);
		if(userService.CancelUserAuthentication(jwt)) {
			// 실제 구현 완료하고 나면 반드시 redirect해야 함
			return "redirect:/";
		} else {
			log.error("logout 실패");
			return "index";
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<User> signup(
			@Valid @RequestBody UserDto userDto
			) {
		return ResponseEntity.ok(userService.signup(userDto));
	}

	@GetMapping("/user")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserDto> myUserInfo() {
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());	
	}

	@GetMapping("/signup")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserDto> isUsedNickname(String nickname) {
		return ResponseEntity.ok(userService.getUserByNickname(nickname).get());
	}

	@PostMapping("/user/details")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserDto> userInfoEmail(String email) {
		return ResponseEntity.ok(userService.getUserWithAuthorities(email).get());
	}

	//@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/user/{uid}")
	public ResponseEntity<UserDto> userInfoById(@PathVariable("uid") Long uid) {
		return ResponseEntity.ok(userService.getAnyUserById(uid).get());
	}

	@GetMapping("/user/list")
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDto>> allUserList() {
		return ResponseEntity.ok(userService.getAllUserList());
	}

	@GetMapping("/user/search")
	public ResponseEntity<List<UserDto>> userListByNickname(String nickname) {
		return ResponseEntity.ok(userService.getUserListBynickname(nickname));
	}

	@PostMapping("/user/modify")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<User> modifyUser (UserDto userDto) {
		return ResponseEntity.ok(userService.updateUser(userDto));
	}

	@PostMapping("/user/modifyPassword")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<Boolean> modifyUserPassword (String email, String password) {
		return ResponseEntity.ok(userService.updateUserPassword(email, password));
	}

	@PostMapping("user/delete")
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<Boolean> deleteUser(String email) {
		return ResponseEntity.ok(userService.deleteUser(email));
	}

}
