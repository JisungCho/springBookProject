
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
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
	
	//내 글 목록
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myBoard")
	public String myBoard(Criteria cri,Model model,Authentication auth) { //내 글목록
		log.info("내 글 목록");
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername(); //내 아이디
		
		log.info("총 게시물 수 : " + infoService.myTotal(userid)); // 내가 작성한 게시물 수  

		List<BoardVO> myList = infoService.list(cri, userid); // 글쓴 목록 가져옴
		log.info("게시물 : "+myList);
		
		model.addAttribute("list", myList); //내가 쓴글 목록
		model.addAttribute("pageMaker", new PageDTO(cri, infoService.myTotal(userid)));
		return "/info/myBoard";
	}
	
	//북마크 설정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myFavorite")
	public void myFavorite(Criteria cri,Model model,Authentication auth) { 
		log.info("북마크");
		log.info("cri : "+cri);
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername(); //현재 로그인한 회원의 아이디
		
		List<FavoriteVO> books = infoService.bookList(cri,userid); //나의 북마크 목록
		//내 아이디 , 북마크 목록 , 페이지메이커를 전달
		model.addAttribute("userid", userid);
		model.addAttribute("books", books);
		model.addAttribute("pageMaker", new PageDTO(cri, infoService.bookTotal(userid)));
	}
	
	//내 정보 보기
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/myInfo")
	public void myFavorite(Model model,Authentication auth) { // 내 정보수정
		log.info("내정보");
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userid = userDetails.getUsername(); // 현재 회원의 아이디
		MemberVO member = infoService.myInfo(userid); //현재 회원의 아이디를 통해 회원정보 가져오기
		model.addAttribute("member",member); 
	}
	
	//내 정보 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<String> updateMyinfo(@RequestBody MemberVO vo) {
		log.info("업데이트");
		infoService.updateInfo(vo); //회원 정보 수정
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/delete/{favoriteId}") 
	@ResponseBody 
	public ResponseEntity<String> delete(@PathVariable Long favoriteId) { //북마크 제거
		return infoService.delete(favoriteId) == 1 ? new ResponseEntity<String>(HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
