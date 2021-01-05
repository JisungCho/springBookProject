package com.jisung.service;

import java.util.List;

import com.jisung.domain.MyAlarm;

public interface MyAlarmService {
	public void insertMyAlarm(MyAlarm myAlarm); //댓글 작성 시  DB등록
	public List<MyAlarm> getMyAlarm(String receiverId);//해당 아이디의 알람목록
	public int countMyAlarm(String receiverId); // checked 조회
}
