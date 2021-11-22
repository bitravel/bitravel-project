package com.bitravel.controller;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.bitravel.data.dto.UserDto;
import com.bitravel.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebMvcTest(UserController.class)

@RequiredArgsConstructor
@Slf4j
public class UserControllerTest {

	MockMvc mvc;

	@MockBean
	private UserService userService;

	/*
	 * @Before public void setup() { this.mvc =
	 * MockMvcBuilders.webAppContextSetup(ctx) .addFilters(new
	 * CharacterEncodingFilter("UTF-8", true)) .alwaysDo(print()) .build(); }
	 */

	@Test
	void getUserList() throws Exception {
		// 비교할 알맞은 결과
		List<UserDto> givenList = new ArrayList<>();
		for(int i=0;i<10;i++) {
			givenList.add(userService.getAnyUserById((long) 1).get());
		}
		//given(userService.getAllUserList()).willReturn(givenList);

		// 유저 목록 가져오기
	//	final ResultActions actions = mvc.perform(get("/api/user/list"))
	//			.contentType(MediaType.APPLICATION_JSON)
	//			.andDo(print());

	//	actions
	//	.andExpect(status.isOk())
	//	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	//	.andExpect(content().string("spacekay@kakao.com"))
	//	.andDo(print());

	}


}