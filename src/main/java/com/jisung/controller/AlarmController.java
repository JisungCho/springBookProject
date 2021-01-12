package com.jisung.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jisung.domain.MyAlarm;
import com.jisung.domain.ReplyVO;
import com.jisung.service.MyAlarmService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/alarm")
@Log4j
public class AlarmController {
	
	@Autowired
	MyAlarmService myAlarmService;
	
	//Json데이터를 받아서 사용하고 리턴값을 일반 문자열
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/saveAlarm",consumes = "application/json",produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> insertAlarm(@RequestBody MyAlarm myAlarm) { // 들어온 댓글 생성
		log.info("알람 등록  "+myAlarm);
		//db등록
		myAlarmService.insertMyAlarm(myAlarm);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/getAlarm",produces = "application/json;charset=UTF-8")
	public @ResponseBody ResponseEntity<List<MyAlarm>> getAlarm(Authentication authentication) { // 들어온 댓글 생성
		log.info("알람목록 가져오기  ");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
		String receiverId = userDetails.getUsername();
		log.info("알람목록 유저이름 : "+receiverId);
		//db등록
		List<MyAlarm> alarm = myAlarmService.getMyAlarm(receiverId);
		log.info("알람목록 : "+alarm);
		return new ResponseEntity<List<MyAlarm>>(alarm, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value="/allChecked")
	public @ResponseBody ResponseEntity<String> allChecked(Authentication authentication,HttpServletRequest request){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
		String receiverId = userDetails.getUsername();
		log.info("checked의 유저이름 : "+receiverId);
		myAlarmService.updateChecked(receiverId);
		HttpSession session = request.getSession();
		session.setAttribute("count", 0);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/countAlarm",consumes = "application/json;charset=UTF-8",produces = "text/plain;charset=UTF-8" )
	public @ResponseBody ResponseEntity<String> countAlarm(Authentication authentication,HttpServletRequest request){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
		String receiverId = userDetails.getUsername();
		log.info("조사할 countAlarm의 유저 이름: : "+receiverId);
		log.info("countAlarm의 count: "+myAlarmService.countMyAlarm(receiverId));
		HttpSession session = request.getSession();
		session.setAttribute("count", myAlarmService.countMyAlarm(receiverId));
		return new ResponseEntity<String>(String.valueOf(myAlarmService.countMyAlarm(receiverId)),HttpStatus.OK);
	}
}
