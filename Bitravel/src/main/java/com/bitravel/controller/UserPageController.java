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
    public String openRegisterPage() {
    	return "/user/signUp";
    }
	
}
