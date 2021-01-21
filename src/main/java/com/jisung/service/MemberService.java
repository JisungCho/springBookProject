package com.jisung.service;

import com.jisung.domain.AuthVO;
import com.jisung.domain.MemberVO;

public interface MemberService {
	public void join(MemberVO member,AuthVO auth); //회원가입
	public boolean checkId(String userid); // 아이디 중복검사
	public int MemberCount(String role); // 해당 롤을 가진 회원의 수
}
