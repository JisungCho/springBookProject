package com.jisung.service;

import java.util.List;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
import com.jisung.domain.MemberVO;

public interface InfoService {
	public List<BoardVO> list(Criteria cri,String userid);//내가 쓴글 목록
	public int myTotal(String userid);//내가 쓴 게시물 수
	public List<FavoriteVO> bookList(Criteria cri,String userid); // 북마크 목록
	public int bookTotal(String userid); //좋아요수
	public int delete(Long favoriteId); //북마크 삭제
	public MemberVO myInfo(String userid); // 내 정보 가져오기
	public int updateInfo(MemberVO vo); // 내 정보 업데이트
	
}
