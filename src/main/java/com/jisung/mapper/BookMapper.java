package com.jisung.mapper;

import com.jisung.domain.BookVO;

public interface BookMapper {
	public void insert(BookVO book);
	public int update(BookVO book);
	public int delete(Long boardId);
}
