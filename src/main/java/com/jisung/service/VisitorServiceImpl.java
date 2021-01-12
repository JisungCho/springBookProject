package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jisung.mapper.VisitorMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class VisitorServiceImpl implements VisitorService{

	@Autowired
	VisitorMapper visitorMapper;
	
	@Override
	public void insertDate() {
		//방문자 추가
		log.info("방문자 추가");
		visitorMapper.insert();
	}

	@Override
	public int today() {
		log.info("오늘 방문자 수 구하기");
		return visitorMapper.selectToday();
	}

	@Override
	public int yesterday() {
		log.info("어제 방문자 수 구하기");
		return visitorMapper.selectYesterday();
	}

	@Override
	public int twoDaysAgo() {
		log.info("그제 방문자 수 구하기");
		// TODO Auto-generated method stub
		return visitorMapper.selectTwoDaysAgo();
	}
	

}
