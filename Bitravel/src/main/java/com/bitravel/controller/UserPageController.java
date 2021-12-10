package com.bitravel.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserPageController {
	
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String openLoginPage() {
        return "user/login";
    }
    
    @GetMapping("")
    public String openIndexPage() {
    	return "index";
    }
    
    @GetMapping("/signup")
    public String openFirstSignUpPage() {
    	return "user/signUp";
    }
    
    @GetMapping("/signup/second")
    public String openSecondSignUpPage
    (@RequestParam("userEmail") String email, Model model) {
    	model.addAttribute("userEmail", email);
    	return "user/signUp2";
    }
    
    @GetMapping("/signup/third")
    public String openThirdSignUpPage
    (@RequestParam("userEmail") String email, Model model) {
    	model.addAttribute("userEmail", email);
    	return "user/signUp3";
    }
    
    
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String openAdminPage() {
    	return "user/admin";
    }
    
    @GetMapping("/mypage")
    //@PreAuthorize("isAnonymous()")
    public String openMyPage() {
        return "user/mypage";
    }
	
}
