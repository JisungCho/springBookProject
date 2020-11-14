package com.jisung.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@GetMapping("/customLogin") //로그인 페이지 이동
	public void loginInput(String error,String logout,Model model) {

		if(error != null) {
			model.addAttribute("error","Login Error Check Your Account");
		}
		
		if(logout != null) {
			model.addAttribute("logout","Logout!!");
		}
	}
	
	@GetMapping("/join") // 회원가입 페이지 이동
	public String joinForm() {
		log.info("Join");
		return "customJoin";
	}
	
	@PostMapping("/join") //회원가입
	public String join(MemberVO member, AuthVO auth,RedirectAttributes rttr) {
		  BCryptPasswordEncoder newPw = new BCryptPasswordEncoder();
		  String pw = newPw.encode(member.getUserpw()); //암호화된 비밀번호로 바꿈
		  member.setUserpw(pw);
		  auth.setUserid(member.getUserid());
		  
		 log.info("member "+member);
		 log.info("auth "+auth);
			
		  
		memberService.join(member, auth);//회원 등록처리
		  
		rttr.addFlashAttribute("result","success");

		return "redirect:/board/";
		
	}
	
	@PostMapping(value = "/checkId" ,consumes = "application/json" ,produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public ResponseEntity<String> checkId(@RequestBody String userid){
		log.info("회원가입");
		log.info("아이디 체크 "+userid);
		
		boolean result = memberService.checkId(userid);
		if(result == true) { //중복 안된경우
			return new ResponseEntity<String>(HttpStatus.OK);
		}else {
			log.info("아이디 중복");
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
