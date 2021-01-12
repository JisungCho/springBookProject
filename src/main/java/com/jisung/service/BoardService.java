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
	public BoardVO get(Long boardId); // 해당 게시물 가져오기
	public int total(Criteria cri); // 개시물 수 구하기
	public boolean modify(BoardVO board,BookVO book); // 게시물 수정
	public boolean remove(Long boardId); //게시물 삭제
	
	public void favoriteRegister(FavoriteVO vo); // 좋아요 등록
	public boolean favoriteCheck(String userid,String url); //좋아요 체크
	public boolean favoriteRemove(FavoriteVO vo); //좋아요 제거
	
	//날짜별 게시물 수 
	public int today();
	public int yesterday();
	public int twoDaysAgo();
}
