package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jisung.domain.Criteria;
import com.jisung.domain.ReplyPageDTO;
import com.jisung.domain.ReplyVO;
import com.jisung.mapper.ReplyMapper;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Override
	public int register(ReplyVO vo) {
		log.info("register");
		return replyMapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long replyId) {
		log.info("get");
		return replyMapper.read(replyId);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify");
		return replyMapper.update(vo);
	}

	@Override
	public int remove(ReplyVO vo) {
		log.info("remove");
		return replyMapper.delete(vo);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long boardId) {
		log.info("Reply list");
		return replyMapper.listWithPaging(cri, boardId);
	}

	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long boardId) {
		log.info("reply page list");
		//해당 게시글의 댓글 수 , 현재 페이지의 댓글목록
		return new ReplyPageDTO(replyMapper.total(boardId), replyMapper.listWithPaging(cri, boardId));
	}
	

}
