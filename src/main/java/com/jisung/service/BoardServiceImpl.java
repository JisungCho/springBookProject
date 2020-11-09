package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
import com.jisung.mapper.BoardMapper;
import com.jisung.mapper.BookMapper;
import com.jisung.mapper.FavoriteMapper;
import com.jisung.mapper.ReplyMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private FavoriteMapper favoriteMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board,BookVO book) {
		
		boardMapper.insertSelectKey(board);
		log.info("게시물번호 : "+board.getBoardId());
		
		book.setBoardId(board.getBoardId());
		
		bookMapper.insert(book);
		
		log.info("등록완료");
		
		
	}

	@Override
	public List<BoardVO> list(Criteria cri) {
		log.info("게시물 리스트 출력");
		log.info("get List with criteria : "+cri);
		return boardMapper.listWithPaging(cri);
	}

	@Override
	public BoardVO get(Long boardId) {
		log.info("해당 게시물 정보");
		return boardMapper.get(boardId);
	}

	@Override
	public int total(Criteria cri) {
		log.info("총 게시물의 갯수");
		return boardMapper.total(cri);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board,BookVO book) {
		log.info("글 수정");
		boolean result = boardMapper.update(board) == 1 && bookMapper.update(book) == 1;
		
		return result;
	}

	
	@Transactional
	@Override
	public boolean remove(Long boardId) {
		log.info("글 삭제");
		//자식 테이블 먼저 삭제
		boolean result = replyMapper.deleteAll(boardId)>0 && bookMapper.delete(boardId) == 1 && boardMapper.delete(boardId) == 1;
		
		return result;
	}

	@Override
	public void favoriteRegister(FavoriteVO vo) {
		log.info("좋아요 추가");
		favoriteMapper.insert(vo);
	}

	@Override
	public boolean favoriteCheck(String userid, Long bookId) {
		log.info("좋아요 체크");
		int count = favoriteMapper.prevent_dup(userid, bookId);
		if(count > 0) { // 좋아요를 누른 상태이면
			log.info("좋아요 눌림");
			return true;
		}else { // 좋아요를 안눌른 상태
			log.info("좋아요 안눌림");
			return false;
		}
	}

	@Override
	public boolean favoriteRemove(FavoriteVO vo) {
		log.info("좋아요 제거");
		int result = favoriteMapper.delete(vo);
		return result == 1 ? true : false;
	}



}
