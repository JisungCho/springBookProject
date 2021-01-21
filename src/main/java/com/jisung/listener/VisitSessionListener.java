package com.jisung.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jisung.service.VisitorService;

import lombok.extern.log4j.Log4j;
import org.springframework.security.web.session.HttpSessionEventPublisher;

//리스너
@WebListener
@Log4j
public class VisitSessionListener extends HttpSessionEventPublisher {
	
	@Autowired
	VisitorService visitorService;
	
	//방문자수 카운트
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//새로운 세션이면 현재 날짜로 데이터를 넣어줌
		if(se.getSession().isNew()) {
			getVisitorService(se).insertDate();
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		
	}
	
	//weblistner는 의존성 주입전에 로드되므로 autowired를 사용하면 안되고 밑에처럼 직접 넣어줘야한다.
	private VisitorService getVisitorService(HttpSessionEvent se) {
		    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(se.getSession().getServletContext());
		    return (VisitorService) context.getBean("visitorServiceImpl");
	} 

}
