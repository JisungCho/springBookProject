package com.jisung.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.jisung.domain.MyAlarm;
import com.jisung.domain.OAuthToken;
import com.jisung.service.MemberService;
import com.jisung.service.MyAlarmService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class KakaoController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	MyAlarmService myAlarmService;
	
	//비밀번호 , 비밀번호를 바꾸면 안됨
	String tempPwd = "1234";
	String userid;
	
	//스프링 시큐리티가 동작하기 위해서는 AuthenticationManager라는 존재 필요
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/kakao")
	public String home(String code,MemberVO member,AuthVO auth,HttpServletRequest request){
		
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
		//인가 코드가 리다이렉트된 URI
		params.add("redirect_uri", "http://localhost:8090/kakao");
		//인가 코드 받기 요청으로 얻은 인가 코드
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
		
		//readValue => 첫번째 인자를 두번째 타입으로 변환한다.
		//json -> java object로 변환
		try {
			oauthToken =  objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//가져온 엑세스 토큰
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
		
		//json -> java object로 변환
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
		log.info("블로그서버 패스워드 : "+tempPwd);
		
		//중복확인
		boolean result = memberService.checkId(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
		
		if(result) { //중복 안된경우 회원가입 처리
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
		//인증처리
		Authentication authentication 	= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId(), tempPwd));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//로그인한 회원에게 새로운 알림이 있는지 확인
		HttpSession session = request.getSession();
		//새로운 알람의 갯수확인
		int count = myAlarmService.countMyAlarm(kaKaoProfile.getKakao_account().getEmail()+"_"+kaKaoProfile.getId());
		if(count > 0) {
			log.info("알람 있음");
			session.setAttribute("count",count);
		}else {
			log.info("알람 없음");
			session.setAttribute("count", 0);
		}
		
		return "redirect:/";
	}
}
