package com.jisung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
