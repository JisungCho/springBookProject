package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;

public interface BookMapper {
	public void insert(BookVO book);
	public int update(BookVO book);
	public int delete(Long boardId);
	public List<BookVO> read(@Param("cri")Criteria cri, @Param("userid")String userid);
	public int total(String userid);
}
