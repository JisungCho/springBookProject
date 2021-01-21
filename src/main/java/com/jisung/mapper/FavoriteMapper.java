package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;

public interface FavoriteMapper {
	public int insert(FavoriteVO vo); //넣기
	public int delete(Long favoriteId);//삭제
	public Long prevent_dup(@Param("userid") String userid, @Param("url")String url); //북마크 중복확인
	public List<FavoriteVO> read(@Param("cri")Criteria cri, @Param("userid")String userid); //내 북마크 목록
	public int total(String userid); //내 북마크 갯수
	public Long get(@Param("userid") String userid, @Param("url")String url); //내가 북마크한 책의 favoriteId
}
