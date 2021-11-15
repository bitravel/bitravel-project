package com.bitravel.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void createUser_JSON() throws Exception {
        String userJson ="{\"username\":\"dsunni\", \"password\":\"123\"}";

		/*
		 * mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
		 * .contentType(MediaType.APPLICATION_JSON_UTF8)
		 * .accept(MediaType.APPLICATION_JSON_UTF8) .content(userJson))
		 * .andExpect(status().isOk()) .andExpect(jsonPath("$.username",
		 * is(equalTo("bsjhihi")))) .andExpect(jsonPath("$.password",
		 * is(equalTo("sisi"))));
		 */
    }
}