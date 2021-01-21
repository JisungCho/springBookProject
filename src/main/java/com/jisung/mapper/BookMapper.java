package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;

public interface BookMapper {
	public void insert(BookVO book);//넣기
	public int update(BookVO book);//수정
	public int delete(Long boardId);//삭제
}
