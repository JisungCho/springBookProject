package com.jisung.mapper;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.FavoriteVO;

public interface FavoriteMapper {
	public int insert(FavoriteVO vo);
	public int delete(FavoriteVO vo);
	public int prevent_dup(@Param("userid") String userid, @Param("bookId")Long bookId);
}
