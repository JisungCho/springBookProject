package com.jisung.service;

import com.jisung.domain.AuthVO;
import com.jisung.domain.MemberVO;

public interface MemberService {
	public void join(MemberVO member,AuthVO auth);
	public boolean checkId(String userid);
	public int MemberCount(String role);
}
