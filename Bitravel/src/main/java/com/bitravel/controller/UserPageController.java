package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String openSecondSignUpPage
    (@RequestParam("nickname") String nickname, Model model) {
    	model.addAttribute("nickname", nickname);
    	return "/user/signUp2";
    }
    
    @GetMapping("/signup/third")
    public String openThirdSignUpPage() {
    	return "/user/signUp3";
    }
	
}
