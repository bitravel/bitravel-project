package com.bitravel.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitravel.data.dto.LoginDto;
import com.bitravel.data.dto.TokenDto;
import com.bitravel.data.dto.UserDto;
import com.bitravel.data.dto.UserTravelDto;
import com.bitravel.data.entity.User;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.jwt.TokenProvider;
import com.bitravel.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
@Api(value = "UserController")
public class UserController {

	private final UserService userService;
	private final TokenProvider tokenProvider;

	@PostMapping("/login")
	public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) throws IOException {
		log.info(loginDto.getEmail()+" check");
		try {
			TokenDto tokenInfo = userService.getUserAuthentication(loginDto);
			if(tokenInfo==null) {
				response.setStatus(401);
				return null;
			}
			String jwt = tokenInfo.getToken();
			jwt = "Bearer"+jwt;
			tokenInfo.setToken(jwt);
			
			tokenProvider.createCookie(response, jwt);
			
			return ResponseEntity.ok(tokenInfo);
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setStatus(500);
			return null;
		}
	}

	@GetMapping("/logout")
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		String[] Cookies = request.getHeader("Cookie").split("; ");
		String bearerToken = "";
		for(int i=0;i<Cookies.length;i++) {
			if(Cookies[i].indexOf(JwtFilter.AUTHORIZATION_HEADER)>-1) {
				bearerToken = Cookies[i].replace(JwtFilter.AUTHORIZATION_HEADER, "");
				break;
			}
		}
		String jwt = bearerToken.substring(1);
		if(userService.CancelUserAuthentication(jwt)) {
			// ?????? ?????? ???????????? ?????? ????????? redirect?????? ???
			return "redirect:/";
		} else {
			log.error("logout ??????");
			return "index";
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<User> signup(
			@Valid @RequestBody UserDto userDto
			) {
		return ResponseEntity.ok(userService.signup(userDto));
	}
	
    /**
     * UserTravel ?????? ??????
     */
    @PostMapping("/signup/fin")
    @ApiOperation(value = "????????? ??????", notes = "????????? ????????? ???????????? API. Travel entity ???????????? ???????????? ????????????.")
    public ResponseEntity<String> saveUserTravels(@RequestBody List<UserTravelDto> params) {   	
		if (params.size()>9) {
			if(!userService.updatePoint(params.get(0).getUserEmail(), 250))
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    	try {		
    		for(int i=0;i<params.size();i++) {
				userService.saveUserTravel(params.get(i).toEntity());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
    }

	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserDto> myUserInfo() {
		Optional<UserDto> userInfo = userService.getMyUserWithAuthorities();
		if(userInfo.isEmpty())
			return ResponseEntity.badRequest().body(null);
		else
			return ResponseEntity.ok(userInfo.get());	
	}

	@GetMapping("/signup")
	public ResponseEntity<UserDto> isUsedNickname(String nickname) {
		return ResponseEntity.ok(userService.getUserByNickname(nickname).get());
	}

	@PostMapping("/user/details")
	public ResponseEntity<UserDto> userInfoEmail(String email) {
		Optional<UserDto> userInfo = userService.getUserWithAuthorities(email);
		if(userInfo.isEmpty())
			return ResponseEntity.badRequest().body(null);
		else
			return ResponseEntity.ok(userInfo.get());
	}

	@GetMapping("/user/{uid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<UserDto> userInfoById(@PathVariable("uid") Long uid) {
		return ResponseEntity.ok(userService.getAnyUserById(uid).get());
	}

	@GetMapping("/user/list")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDto>> allUserList() {
		List<UserDto> list = userService.getAllUserList();
		if(list.size()<1000)
			return ResponseEntity.ok(list);
		else
			return ResponseEntity.ok(list.subList(0, 1000));
	}
	
	@GetMapping("/user/search/nickname")
	public ResponseEntity<List<UserDto>> userListByNickname(String keyword) {
		return ResponseEntity.ok(userService.getUserListBynickname(keyword));
	}
	
	@GetMapping("/user/search/realname")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDto>> userListByRealname(String keyword) {
		return ResponseEntity.ok(userService.getUserListByrealname(keyword));
	}
	
	@GetMapping("/user/search/email")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDto>> userListByEmail(String keyword) {
		return ResponseEntity.ok(userService.getUserListByEmail(keyword));
	}

	@PostMapping("/user/modify")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<User> modifyUser (UserDto userDto) {
		return ResponseEntity.ok(userService.updateUser(userDto));
	}

	@PostMapping("/user/modifyPassword")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<Boolean> modifyUserPassword (String email, String password) {
		return ResponseEntity.ok(userService.updateUserPassword(email, password));
	}

	@GetMapping("user/delete")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<Boolean> deleteUser(String email) {
		return ResponseEntity.ok(userService.deleteUser(email));
	}
	
	@PostMapping("user/delete")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Boolean> deleteUserByList(@RequestBody List<Long> list) {
		return ResponseEntity.ok(userService.deleteUserByList(list));
	}


}
