package com.jisung.mapper;

import java.util.List;

import com.jisung.domain.BoardVO;
import com.jisung.domain.Criteria;

public interface BoardMapper {
	public void insertSelectKey(BoardVO board);
	
	//public List<BoardVO> list();
	
	public List<BoardVO> listWithPaging(Criteria cri);
	
	public BoardVO get(int boardId);
	
	public int total(Criteria cri);
	
	public int update(BoardVO vo);
	
	public int delete(int boardId);
}
