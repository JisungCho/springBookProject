package com.jisung.domain;

import lombok.Data;

@Data
public class MyAlarm {
	private int alarmId;
	private String receiverId;
	private String callerId;
	private String content;
	private boolean checked;
	
}
