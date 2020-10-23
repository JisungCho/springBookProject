package com.jisung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jisung.domain.Criteria;
import com.jisung.domain.ReplyVO;

public interface ReplyMapper {
	public int insert(ReplyVO vo); //등록
	
	public ReplyVO read(Long replyId); //조회
	//Mybatis는 한개의 객체만 던질 수 있음 2개이상일 경우 @param으로 지정
	public List<ReplyVO> listWithPaging(@Param("cri") Criteria cri,@Param("boardId") Long boardId); // 페이징 읽기
	public int delete(Long replyId); //삭제
	public int update(ReplyVO vo); //수정
	public int total(Long boardId);
	
	public int deleteAll(Long boardId);
}
