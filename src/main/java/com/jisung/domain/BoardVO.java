package com.jisung.domain;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoardVO {
	private Long boardId; // 글번호
	private String subject; // 제목
	private String content; // 내용
	private String writer; // 작성자
	private Date regdate;
	
	private BookVO book;

}
