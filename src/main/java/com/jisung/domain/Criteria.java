package com.jisung.domain;

import lombok.Data;

@Data
public class Criteria {
	private int pageNum; // 페이지 번호
	private int amount; // 한 페이지당 보여줄 양
	
	public Criteria() { //기본 1페이지 10개
		this(1,10);
	}
	public Criteria(int pageNum,int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
}
