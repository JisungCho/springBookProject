package com.jisung.service;

import java.util.List;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
import com.jisung.domain.MemberVO;

public interface InfoService {
	public List<BoardVO> list(Criteria cri,String userid);
	public int myTotal(String userid);//게시물수
	public List<FavoriteVO> bookList(Criteria cri,String userid);
	public int bookTotal(String userid); //좋아요수
	public int delete(Long favoriteId); 
	public MemberVO myInfo(String userid);
	public int updateInfo(MemberVO vo);
	
}
