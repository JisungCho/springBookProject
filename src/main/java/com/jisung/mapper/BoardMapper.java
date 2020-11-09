package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.BoardVO;
import com.jisung.domain.Criteria;

public interface BoardMapper {
	public void insertSelectKey(BoardVO board);
	
	//public List<BoardVO> list();
	
	public List<BoardVO> listWithPaging(Criteria cri); // 페이징한 게시물 목록 가져오기 cri에는 pageNum과 amount가 존재
	
	public List<BoardVO> myListWithPaging(@Param("cri") Criteria cri,@Param("userid") String userid);
	
	public BoardVO get(Long boardId);
	
	public int total(Criteria cri);
	
	public int myTotal(@Param("userid") String userid);
	
	public int update(BoardVO vo);
	
	public int delete(Long boardId);
	
	
}
