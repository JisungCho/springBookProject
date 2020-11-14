package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;

public interface FavoriteMapper {
	public int insert(FavoriteVO vo);
	public int delete(Long favoriteId);
	public int prevent_dup(@Param("userid") String userid, @Param("url")String url);
	public List<FavoriteVO> read(@Param("cri")Criteria cri, @Param("userid")String userid);
	public int total(String userid);
	public Long get(@Param("userid") String userid, @Param("url")String url);
}
