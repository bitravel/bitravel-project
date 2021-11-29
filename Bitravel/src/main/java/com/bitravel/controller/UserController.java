package com.bitravel.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
import com.bitravel.data.dto.UserTravelDto;
import com.bitravel.data.entity.User;
import com.bitravel.jwt.JwtFilter;
import com.bitravel.service.UserService;
import com.bitravel.util.ScriptUtil;

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
	
	@PostMapping("/login")
	public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) throws IOException {
		log.info(loginDto.getEmail()+" "+loginDto.getPassword()+" check");
		try {
			TokenDto tokenInfo = userService.getUserAuthentication(loginDto);
			if(tokenInfo==null) {
				ScriptUtil.alertAndBackPage(response, "비밀번호를 다시 입력해 주세요.");
				return null;
			}
			String jwt = tokenInfo.getToken();	
			jwt = "Bearer "+jwt;
			tokenInfo.setToken(jwt);
			return ResponseEntity.ok(tokenInfo);
		} catch (Exception e) {
			ScriptUtil.alertAndBackPage(response, "아이디를 찾을 수 없습니다.");
			return null;
		}
	}

	@PostMapping("/logout")
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
	
    /**
     * UserTravel 신규 등록
     */
    @PostMapping("/signup/fin")
    @ApiOperation(value = "여행지 작성", notes = "여행지 내용을 저장하는 API. Travel entity 클래스로 데이터를 저장한다.")
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
