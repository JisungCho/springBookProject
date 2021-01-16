package com.jisung.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.log4j.Log4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Log4j
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,Authentication auth) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		log.info("현재 권한 : "+authentication.getAuthorities());
		
		//현재 접속한 사람의 권한을 가져와서 관리자인 경우에는 관리자 페이지로 이동 , 관리자가 아닌경우는 일반 페이지로 이동
		boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		if(admin) {
			return "redirect:/admin/";
		}else {
			return "redirect:/board/";
		}
		
	}
	
}
