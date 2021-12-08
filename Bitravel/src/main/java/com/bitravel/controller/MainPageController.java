package com.bitravel.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {
    /**
     * 홈페이지 메인 페이지
     */
    @GetMapping("/main")
    public String openIndexPage() {
    	return "/main";
    }
}