package com.jisung.service;

import java.util.List;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;

public interface BoardService {
	public void register(BoardVO board,BookVO book);
	//public List<BoardVO> list();
	public List<BoardVO> list(Criteria cri);
	public BoardVO get(Long boardId);
	public int total(Criteria cri);
	public boolean modify(BoardVO board,BookVO book);
	public boolean remove(Long boardId);
}
