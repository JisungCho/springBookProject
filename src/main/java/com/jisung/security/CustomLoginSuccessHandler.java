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

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	MyAlarmService myAlarmService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.warn("Login Success");
		
		List<String> roleNames = new ArrayList<>();
		
        //사용자가 가진 모든권한을 가져와서 roleNames에 넣어줌
		authentication.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});
		
		log.warn("ROLE NAMES: "+roleNames);
		
		System.out.println("유저이름:"+authentication.getName());
		//세션에 아이디 저장
		//By 12.31
		//HttpSession session = request.getSession();
		//session.setAttribute("user", authentication.getName());
		
		
		//By 12.31
		//로그인한 회원 notification
		//0.로그인 성공
		//1.해당회원의 목록 가져오기 
		//2.해당회원의 이름으로 checked가 false가 있는지 확인
		//TBL_ALARM에서 checked가 false가 하나라도 있으면 session에 저장
		
		//해당회원의 알람 목록
		List<MyAlarm> myAlarmList = myAlarmService.getMyAlarm(authentication.getName());
		log.info("알람목록 : " +myAlarmList);
		
		HttpSession session = request.getSession();
		if(myAlarmList.size() != 0) {
			session.setAttribute("myAlarmList",myAlarmList);
		}
		
		//로그인한 회원과 checked의 갯수
		log.info("checked : "+authentication.getName()+","+myAlarmService.countMyAlarm(authentication.getName()));
		
		if(myAlarmService.countMyAlarm(authentication.getName()) > 0) {
			log.info("알람 있음");
			session.setAttribute("alarmBell", true);
		}else {
			log.info("알람 없음");
			session.setAttribute("alarmBell", false);
		}
		
		response.sendRedirect("/board/");
	}

}
