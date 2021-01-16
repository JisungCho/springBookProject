package com.jisung.domain;

import jdk.internal.org.jline.utils.Log;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
public class Criteria {
	private int pageNum; // 페이지 번호
	private int amount; // 한 페이지당 보여줄 양
	
	private String type; //검색 타입
	private String keyword; //검색내용
	
	public Criteria() { //기본 : 페이지 번호:1 ,보여줄 양5개
		this(1,5);
	}
	public Criteria(int pageNum,int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() { // 검색 조건이 각 글자로 (T,W,C)로 구성되어 있으므로 검색 조건을 배열로 만들어서 한 번에 처리
		return type==null? new String[] {} : type.split(""); //TW가 들어면 T,W로 쪼개서 배열로 변경
	}
}
