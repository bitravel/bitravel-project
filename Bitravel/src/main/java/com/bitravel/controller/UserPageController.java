package com.bitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
	
    @GetMapping("/login")
    public String openLoginPage() {
        return "/loginPage";
    }
    
    @GetMapping("")
    public String openIndexPage() {
    	return "/index";
    }
    
    @GetMapping("/register")
    public String openRegisterPage() {
    	return "/registerPage";
    }
	
}
