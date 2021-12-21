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
    
    @GetMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public String openFirstSignUpPage() {
    	return "user/signUp";
    }
    
    @GetMapping("/signup/second")
    @PreAuthorize("isAnonymous()")
    public String openSecondSignUpPage
    (@RequestParam("userEmail") String email, Model model) {
    	model.addAttribute("userEmail", email);
    	return "user/signUp2";
    }
    
    @GetMapping("/signup/third")
    @PreAuthorize("isAnonymous()")
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
	
}
