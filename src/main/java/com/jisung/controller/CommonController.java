package com.jisung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.AuthVO;
import com.jisung.domain.MemberVO;
import com.jisung.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth,Model model) {
		log.info("access Denied:" + auth);
		model.addAttribute("msg","Access Denied");
	}
	
	@GetMapping("/customLogin")
	public void loginInput(String error,String logout,Model model) {
		log.info("error: "+error);
		log.info("logout: "+logout);
		
		if(error != null) {
			model.addAttribute("error","Login Error Check Your Account");
		}
		
		if(logout != null) {
			model.addAttribute("logout","Logout!!");
		}
	}
	
	@GetMapping("/join")
	public String joinForm() {
		log.info("Join");
		return "customJoin";
	}
	
	@PostMapping("/join")
	public String join(MemberVO member, AuthVO auth,RedirectAttributes rttr) {
		  BCryptPasswordEncoder newPw = new BCryptPasswordEncoder();
		  String pw = newPw.encode(member.getUserpw()); 
		  member.setUserpw(pw);
		  auth.setUserid(member.getUserid());
		  
		 log.info("member "+member);
		 log.info("auth "+auth);
			
		  
		memberService.join(member, auth);
		  
		rttr.addFlashAttribute("result","success");

		return "redirect:/board/";
		
	}
	
}
