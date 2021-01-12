package com.jisung.mapper;

public interface VisitorMapper {
	public void insert();
	public int selectToday();
	public int selectYesterday();
	public int selectTwoDaysAgo();
}
