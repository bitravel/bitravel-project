package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
	
    @GetMapping("/login")
    public String openLoginPage() {
        return "/user/login";
    }
    
    @GetMapping("")
    public String openIndexPage() {
    	return "/index";
    }
    
    @GetMapping("/signup")
    public String openFirstSignUpPage() {
    	return "/user/signUp";
    }
    
    @GetMapping("/signup/second")
    public String openSecondSignUpPage(String nickname) {
    	return "/user/signUp2?nickname="+nickname;
    }
    
    @GetMapping("/signup/third")
    public String openThirdSignUpPage() {
    	return "/user/signUp3";
    }
	
}
