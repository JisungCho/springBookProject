package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jisung.domain.MyAlarm;
import com.jisung.mapper.MyAlarmMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MyAlarmServiceImpl implements MyAlarmService {

	@Autowired
	MyAlarmMapper myAlarmMapper;
	
	@Override
	public void insertMyAlarm(MyAlarm myAlarm) {
		
		log.info("알람 DB에 저장 "+myAlarm);
		myAlarmMapper.insert(myAlarm);
	}

	@Override
	public List<MyAlarm> getMyAlarm(String receiverId) {
		log.info("해당아이디의 알람목록 가져오기 : "+receiverId);
		return myAlarmMapper.getMyAlarm(receiverId);
	}

	@Override
	public int countMyAlarm(String receiverId) {
		log.info("checked 갯수 가져오기");
		return myAlarmMapper.countMyAlarm(receiverId);
	}

}
