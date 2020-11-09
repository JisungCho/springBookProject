package com.jisung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.MemberVO;
import com.jisung.mapper.BoardMapper;
import com.jisung.mapper.BookMapper;
import com.jisung.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class InfoServiceImpl implements InfoService{

	@Autowired 
	private BoardMapper boardMapper;
	@Autowired
	private BookMapper bookMapper;
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public List<BoardVO> list(Criteria cri,String userid) {
		log.info("나의 글 목록 출력");
		return boardMapper.myListWithPaging(cri, userid);
	}

	@Override
	public int myTotal(String userid) {
		return boardMapper.myTotal(userid);
	}

	@Override
	public List<BookVO> bookList(Criteria cri,String userid) {
		return bookMapper.read(cri,userid);
	}

	@Override
	public int bookTotal(String userid) {
		return bookMapper.total(userid);
	}

	@Override
	public MemberVO myInfo(String userid) {
		
		return memberMapper.read(userid);
	}

	@Override
	public int updateInfo(MemberVO vo) {
		BCryptPasswordEncoder pwe = new BCryptPasswordEncoder();
		String pw = pwe.encode(vo.getUserpw());
		vo.setUserpw(pw);
		return memberMapper.update(vo);
	}
	
}