package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
import com.jisung.domain.MemberVO;
import com.jisung.mapper.BoardMapper;
import com.jisung.mapper.BookMapper;
import com.jisung.mapper.FavoriteMapper;
import com.jisung.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class InfoServiceImpl implements InfoService{

	@Autowired 
	private BoardMapper boardMapper;
	@Autowired
	private FavoriteMapper favoriteMapper;
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public List<BoardVO> list(Criteria cri,String userid) {
		log.info("나의 글 목록 출력");
		return boardMapper.myListWithPaging(cri, userid);
	}

	@Override
	public int myTotal(String userid) {
		log.info("나의 글 갯수");
		return boardMapper.myTotal(userid);
	}

	@Override
	public List<FavoriteVO> bookList(Criteria cri,String userid) {
		log.info("나의 북마크 목록");
		return favoriteMapper.read(cri,userid);
	}

	@Override
	public int bookTotal(String userid) {
		log.info("나의 북마크 갯수");
		return favoriteMapper.total(userid);
	}

	@Override
	public MemberVO myInfo(String userid) {
		log.info("내 정보 가져오기");
		return memberMapper.read(userid);
	}

	@Override
	public int updateInfo(MemberVO vo) {
		log.info("내 정보 업데이트"); 
		if(vo.getUserpw() != null) { //일반 회원인경우(카카오회원은 NULL이 넘어옴)
			log.info("일반회원");
			BCryptPasswordEncoder pwe = new BCryptPasswordEncoder();
			String pw = pwe.encode(vo.getUserpw());
			vo.setUserpw(pw);
		}
		return memberMapper.update(vo);
	}

	@Override
	public int delete(Long favoriteId) {
		log.info("북마크 삭제");
		return favoriteMapper.delete(favoriteId) ;
	}
	
}
