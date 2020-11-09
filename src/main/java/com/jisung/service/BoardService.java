package com.jisung.service;

import java.util.List;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;

public interface BoardService {
	public void register(BoardVO board,BookVO book);
	//public List<BoardVO> list();
	public List<BoardVO> list(Criteria cri); // 게시물 목록 
	public BoardVO get(Long boardId);
	public int total(Criteria cri); // 개시물 수 구하기
	public boolean modify(BoardVO board,BookVO book);
	public boolean remove(Long boardId);
	
	public void favoriteRegister(FavoriteVO vo);
	public boolean favoriteCheck(String userid,Long bookId);
	public boolean favoriteRemove(FavoriteVO vo);
}
