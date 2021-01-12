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
	
	private VisitorService getVisitorService(HttpSessionEvent se) {
		    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(se.getSession().getServletContext());
		    return (VisitorService) context.getBean("visitorServiceImpl");
	} 

}
