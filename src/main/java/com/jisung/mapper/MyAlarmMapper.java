package com.jisung.mapper;

import java.util.List;

import com.jisung.domain.MyAlarm;

public interface MyAlarmMapper {
	public int insert(MyAlarm myAlarm); //알람 정보 넣기
	public List<MyAlarm> getMyAlarm(String receiverId); //내 알람정보 가져오기
	public int countMyAlarm(String receiverId); // 알람의 개수
	public void updateChecked(String receiverId); // 알람 체크 여부
}
