package com.jisung.mapper;

import com.jisung.domain.MemberVO;

public interface MemberMapper {
	public MemberVO read(String userid);
	public int insert(MemberVO vo);
	public int update(MemberVO vo);
	public int checkId(String userid);
	public int memberCount(String role);
}
