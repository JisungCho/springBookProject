package com.jisung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jisung.service.BoardService;
import com.jisung.service.MemberService;
import com.jisung.service.VisitorService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/admin")
@Log4j
public class AdminController {
	
	@Autowired
	VisitorService visitorService;
	@Autowired
	MemberService memberService;
	@Autowired
	BoardService boardService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@RequestMapping("/")
	public String test(Model model) {
		int today = visitorService.today();
		int yesterday = visitorService.yesterday();
		int twoDaysAgo = visitorService.twoDaysAgo();
		
		
		model.addAttribute("today", today);
		model.addAttribute("yesterday", yesterday);
		model.addAttribute("twoDaysAgo", twoDaysAgo);
		
		//카카오 계정과 일반계정의 갯수 구하기
		model.addAttribute("kakao",memberService.MemberCount("ROLE_KAKAO"));
		model.addAttribute("member",memberService.MemberCount("ROLE_MEMBER"));
		
		//게시물 수 
		model.addAttribute("w_today", boardService.today());
		model.addAttribute("w_yesterday", boardService.yesterday());
		model.addAttribute("w_twoDaysAgo", boardService.twoDaysAgo()); 
		
		return "/admin/test";
	
	}
}
