package com.jisung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jisung.domain.AuthVO;
import com.jisung.domain.MemberVO;
import com.jisung.mapper.AuthMapper;
import com.jisung.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private AuthMapper authMapper;

	
	@Transactional
	@Override
	public void join(MemberVO member, AuthVO auth) {
		
		log.info("회원가입");
		
		memberMapper.insert(member);
		authMapper.insert(auth);
	}


	@Override
	public boolean checkId(String userid) {
		log.info("아이디 중복검사");
		int count = memberMapper.checkId(userid);
		if(count > 0) {
			return false; //중복 된 경우
		}else {
			return true; //중복 안된 경우
		}
	}


	@Override
	public int MemberCount(String role) {
		log.info("회원 수 구하기");
		return memberMapper.memberCount(role);
	}

}
