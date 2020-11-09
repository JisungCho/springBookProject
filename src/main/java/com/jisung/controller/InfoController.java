
package com.jisung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.MemberVO;
import com.jisung.domain.PageDTO;
import com.jisung.service.InfoService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/info/")
public class InfoController {
	
	@Autowired
	private InfoService infoService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myBoard")
	public String myBoard(Criteria cri,Model model,Authentication auth) {
		log.info("내 글 목록");
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername();
		log.info("총 게시물 수 : " + infoService.myTotal(userid));

		List<BoardVO> myList = infoService.list(cri, userid);
		log.info("게시물 : "+myList);
		
		model.addAttribute("list", myList);
		model.addAttribute("pageMaker", new PageDTO(cri, infoService.myTotal(userid)));
		return "/info/myBoard";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myFavorite")
	public void myFavorite(Criteria cri,Model model,Authentication auth) {
		log.info("즐겨찾기");
		log.info("cri : "+cri);
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername();
		List<BookVO> books = infoService.bookList(cri,userid);
		model.addAttribute("userid", userid);
		model.addAttribute("books", books);
		model.addAttribute("pageMaker", new PageDTO(cri, infoService.bookTotal(userid)));
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myInfo")
	public void myFavorite(Model model,Authentication auth) {
		log.info("내정보");
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername();
		MemberVO member = infoService.myInfo(userid);
		model.addAttribute("member",member);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<String> updateMyinfo(@RequestBody MemberVO vo) {
		log.info("업데이트");
		log.info(vo);
		infoService.updateInfo(vo);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
