package com.jisung.mapper;

public interface VisitorMapper {
	public void insert(); //방문기록 남기기
	public int selectToday(); // 오늘 날짜의 개수
	public int selectYesterday(); // 어제 날짜의 개수
	public int selectTwoDaysAgo(); //  그제 날짜의 개수
}
