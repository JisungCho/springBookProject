package com.jisung.mapper;

import java.util.List;

import com.jisung.domain.MyAlarm;

public interface MyAlarmMapper {
	public int insert(MyAlarm myAlarm);
	public List<MyAlarm> getMyAlarm(String receiverId);
	public int countMyAlarm(String receiverId);
}
