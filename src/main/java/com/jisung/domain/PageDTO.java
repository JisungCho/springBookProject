package com.jisung.domain;

import lombok.Data;

@Data
public class PageDTO {
	private int startPage; //시작페이지
	private int endPage; //끝페이지
	private boolean prev , next; //이전 ,다음 존재 여부
	private int total; //전체 수
	private Criteria cri; // 페이지번호, 보여줄양
	
	public PageDTO(Criteria cri,int total) {
		this.cri = cri;
		this.total = total;
		
		//올림
		/*
		// pageNum : 1 -> 1 /10.0 -> 1 -> 1*10 -> 10
		// pageNum : 2 -> 2 / 10.0 -> 1 -> 1*10 -> 10
		// .
		// . 
		// .
		// pageNum : 10 -> 10/10.0 -> 1 -> 1*10 -> 10
		// pageNum : 11 -> 11/10.0 -> 1.1 -> 2*10 -> 20
		*/
		this.endPage = (int)(Math.ceil(cri.getPageNum() / 10.0))*10;
		
		this.startPage = this.endPage - 9; // 첫페이지
		
		//진짜 끝페이지는 전체 데이터수를 페이지당 보여줄 양으로 나누어줌
		// 38 / 5 -> 7.6 -> 총 필요한 페이지 8개 
		int realEnd = (int) (Math.ceil((total*1.0)/cri.getAmount()));
		
		//진짜 끝페이지가 기본으로 구한 끝페이지보다 작으면 끝페이지를 진짜 끝페이지로 바꿔줌
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		//이전 다음 존재여부
		this.prev = this.startPage > 1; 
		this.next = this.endPage < realEnd;
	}
	
}
