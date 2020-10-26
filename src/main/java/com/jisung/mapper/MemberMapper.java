package com.jisung.mapper;

import com.jisung.domain.MemberVO;

public interface MemberMapper {
	public MemberVO read(String userid);
	public int insert(MemberVO vo);
}
