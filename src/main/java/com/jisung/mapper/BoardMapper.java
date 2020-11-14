package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.BoardVO;
import com.jisung.domain.Criteria;

public interface BoardMapper {
	public void insertSelectKey(BoardVO board);
		
	public List<BoardVO> listWithPaging(Criteria cri); //전체 글 목록
	
	public List<BoardVO> myListWithPaging(@Param("cri") Criteria cri,@Param("userid") String userid); //내가 쓴 글 목록
	
	public BoardVO get(Long boardId); //글 정보
	
	public int total(Criteria cri); //전체 글 갯수
	
	public int myTotal(@Param("userid") String userid); // 내가 쓴 글 갯수
	
	public int update(BoardVO vo); //글 수정
	
	public int delete(Long boardId); //업데이트
	
	
}
