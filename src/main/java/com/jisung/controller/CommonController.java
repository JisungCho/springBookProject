package com.jisung.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.AuthVO;
import com.jisung.domain.MemberVO;
import com.jisung.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonController {

	@Autowired
	private MemberService memberService;
	
	//메일 전송
	@Autowired
	private JavaMailSender mailSender;

	// spring security 권한 없는 사용자 접근시 처리
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {
		log.info("access Denied:" + auth);
		model.addAttribute("msg", "Access Denied");
	}

	@GetMapping("/customLogin") // 로그인 페이지 이동
	public void loginInput(String error, String logout, Model model) {
		if (error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}
		if (logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}

	@GetMapping("/join") // 회원가입 페이지 이동
	public String joinForm() {
		log.info("Join");
		return "customJoin";
	}

	@PostMapping("/join") // 회원가입
	public String join(MemberVO member, AuthVO auth, RedirectAttributes rttr) {
		// 비밀번호 암호화
		BCryptPasswordEncoder newPw = new BCryptPasswordEncoder();
		String pw = newPw.encode(member.getUserpw()); // 암호화된 비밀번호로 바꿈
		member.setUserpw(pw);
		auth.setUserid(member.getUserid());

		memberService.join(member, auth);// 회원 등록처리

		rttr.addFlashAttribute("result", "success");

		return "redirect:/board/";

	}

	//아이디 중복검사
	@PostMapping(value = "/checkId", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> checkId(@RequestBody String userid) {
		log.info("아이디 체크 " + userid);

		boolean result = memberService.checkId(userid);
		if (result == true) { // 중복 안된경우
			return new ResponseEntity<String>(HttpStatus.OK);
		} else { //중복된 경우
			log.info("아이디 중복");
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/mailCheck",consumes = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String mailCheck(String email) throws Exception{
		log.info("이메일 데이터 전송확인");
		log.info("인증 메일 : "+email);
		
		Random random = new Random();
		int checkNum = random.nextInt(888888)+111111; // 111111 - 999999
		log.info("인증번호 : "+checkNum);
		
		//이메일 보내기
		String setFrom = "jisung1367@gmail.com";
		String toEmail = email;
		String title = "독거노인 회원가입 인증 이메일 입니다.";
		String content = "독거노인에 가입해주셔서 감사합니다."+ "<br/><br/>"+"인증 번호는 "+checkNum+" 입니다.<br/>"+
							"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        String num = Integer.toString(checkNum);
        return num;
	}

}
