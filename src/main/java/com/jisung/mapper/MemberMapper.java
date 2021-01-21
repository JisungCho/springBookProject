package com.jisung.mapper;

import com.jisung.domain.MemberVO;

public interface MemberMapper {
	public MemberVO read(String userid); //해당 유저 정보
	public int insert(MemberVO vo); // 유저정보 넣기
	public int update(MemberVO vo); // 수정
	public int checkId(String userid); // 아이디 중복확인
	public int memberCount(String role); // 특정 롤에 해당하는 회원 수 구하기
}
