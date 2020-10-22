package com.jisung.service;

import java.util.List;

import com.jisung.domain.Criteria;
import com.jisung.domain.ReplyVO;

public interface ReplyService {
	public int register(ReplyVO vo); //등록
	
	public ReplyVO get(Long replyId); //조회
	
	public int modify(ReplyVO vo); //수정
	
	public int remove(Long replyId); //삭제
	
	public List<ReplyVO> getList(Criteria cri,Long boardId); //목록 
}
