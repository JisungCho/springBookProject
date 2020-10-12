package com.jisung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.mapper.BoardMapper;
import com.jisung.mapper.BookMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board,BookVO book) {
		
		boardMapper.insertSelectKey(board);
		log.info("게시물번호 : "+board.getBoardId());
		
		book.setBoardId(board.getBoardId());
		
		bookMapper.insert(book);
		
		log.info("등록완료");
		
		
	}

}
