package com.jisung.service;

import java.util.List;

public interface VisitorService {
	public void insertDate(); // 방문기록 등록
	public int today(); // 오늘 방문자수
	public int yesterday(); // 어제 방문자수
	public int twoDaysAgo(); // 그제 방문자 수
}
