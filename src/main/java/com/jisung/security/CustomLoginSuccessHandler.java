package com.jisung.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.jisung.domain.MyAlarm;
import com.jisung.service.MyAlarmService;
import com.jisung.service.VisitorService;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	MyAlarmService myAlarmService;
	@Autowired
	VisitorService visitorService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.warn("Login Success");
		
		List<String> roleNames = new ArrayList<>();
		
        //사용자가 가진 role을 가져옴
		authentication.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});
		
		log.warn("ROLE NAMES: "+roleNames);
		
		//만약에 ADMIN 계정이라면
		if(roleNames.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/");
			return;
		}
		
		//By 12.31
		//로그인한 회원 notification
		//0.로그인 성공
		//1.해당회원의 목록 가져오기 
		//2.해당회원의 이름으로 checked가 false가 있는지 확인
		//TBL_ALARM에서 checked가 false가 하나라도 있으면 session에 저장
		
		HttpSession session = request.getSession();
		
		//로그인한 회원에게 새로운 알림이 있는지 확인
		int count = myAlarmService.countMyAlarm(authentication.getName());
		if(myAlarmService.countMyAlarm(authentication.getName()) > 0) {
			log.info("알람 있음");
			session.setAttribute("count", count);
		}else {
			log.info("알람 없음");
			session.setAttribute("count", 0);
		}
		
		
		response.sendRedirect("/board/");
	}

}
