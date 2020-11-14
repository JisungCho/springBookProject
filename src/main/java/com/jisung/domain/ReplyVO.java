package com.jisung.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReplyVO {
	private Long replyId; //pk
	private Long boardId; //FK
	private String reply;
	private String replyer;
	//JSON으로 전달 시 해당 형식으로 전송
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm" , timezone = "Asia/Seoul")
	private Date replyDate;
}
