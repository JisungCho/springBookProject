package com.jisung.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jisung.domain.AuthVO;
import com.jisung.domain.KaKaoProfile;
import com.jisung.domain.MemberVO;
import com.jisung.domain.OAuthToken;
import com.jisung.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class KakaoController {
	@Autowired
	private MemberService memberService;
	
	String tempPwd = "1234";
	String userid;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/kakao")
	public String home(String code,MemberVO member,AuthVO auth){
		//#############################토큰요청
		//여기서 POST 방식으로 key-value타입의 데이터를 요청 (카카오쪽으로)
		//이때 RestTemplate 라이브러리 사용
		RestTemplate rt = new RestTemplate();
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		//내가 보낼 데이터가 key=value데이터임을 알려줌
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				//HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "61047743b1338b7db2c2102e0a34b29c");
		params.add("redirect_uri", "http://localhost:8090/kakao");
		params.add("code", code);
					
		//HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		//Http 요청하기 - Post방식으로 - 그리고 reponse 변수에 응답 받음
		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);
		
		// Gson , Json Simple , ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken =  objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
		
		//##############################토큰을 이용한 정보 요청
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		//내가 보낼 데이터가 key=value데이터임을 알려줌
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		//HttpBody 오브젝트 생성
		
		//HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>( headers2);
		
		//Http 요청하기 - Post방식으로 - 그리고 reponse 변수에 응답 받음
		ResponseEntity<String> response2 = rt2.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileRequest2,
			String.class
		);		
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KaKaoProfile kaKaoProfile = null;
		try {
				kaKaoProfile = objectMapper2.readValue(response2.getBody(), KaKaoProfile.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("카카오 아이디(번호) : "+kaKaoProfile.getId());
		log.info("카카오 이메일 : "+kaKaoProfile.getKakao_account().getEmail());
		
		log.info("유저네임 : "+kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
		log.info("블로그 서버 이메일 "+kaKaoProfile.getKakao_account().getEmail());
		//임시패스워드
		//UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		//UUID garbagePassword = UUID.randomUUID();
		log.info("블로그서버 패스워드 : "+tempPwd);
		
		boolean result = memberService.checkId(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
		
		if(result) { //중복 안된경우
			member.setUserid(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
			member.setUserName(kaKaoProfile.getKakao_account().getProfile().nickname);
			member.setUserpw(tempPwd);
			BCryptPasswordEncoder newPw = new BCryptPasswordEncoder();
			String pw = newPw.encode(member.getUserpw()); 
			member.setUserpw(pw);
			auth.setUserid(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
			auth.setAuth("ROLE_KAKAO");
			memberService.join(member, auth);
		}
		
		System.out.println("자동 로그인을 진행합니다.");
		//로그인처리
		Authentication authentication 	= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId(), tempPwd));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
	}
}
