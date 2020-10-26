package com.jisung.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jisung.domain.MemberVO;
import com.jisung.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private MemberMapper membermapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.warn("Load User By UserName : "+username);
		
		//userName means userid
		MemberVO vo = membermapper.read(username);
		
		log.warn("queried by member mapper : "+vo);
		return vo == null ? null : new CustomUser(vo);
	}

}
