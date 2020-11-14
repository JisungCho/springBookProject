package com.jisung.controller;

import java.awt.print.Book;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
import com.jisung.domain.FavoriteVO;
import com.jisung.domain.PageDTO;
import com.jisung.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/") 
	public String list(Model model, Criteria cri) { //메인 화면 
		log.info("메인화면");
		log.info("총 게시물 수 : " + boardService.total(cri)); // 총 게시물 수 

		List<BoardVO> boardList = boardService.list(cri); //페이징한 게시물 목록
		model.addAttribute("list", boardList); // main.jsp에 전달
		model.addAttribute("pageMaker", new PageDTO(cri, boardService.total(cri))); //총 게시물 수와 pageNum , amount 전달
		return "/board/main";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register") 
	public void register() { //글 등록 페이지로 이동
		log.info("글 등록 페이지 이동");
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String register(BoardVO board, BookVO book, RedirectAttributes rttr) {
		log.info("글 등록처리");
		
		boardService.register(board, book); // 글 등록

		rttr.addFlashAttribute("result", board.getBoardId()); //게시물번호를 결과로 전달

		return "redirect:/board/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/bookSearch")
	public void bookSearch() { //책 검색 페이지로 이동
		log.info("책 검색페이지");

	}
	
	@GetMapping("/get")
	public void get(Long boardId, Model model, @ModelAttribute("cri") Criteria cri,Authentication auth) { //게시물 상세조회
		
		BoardVO vo = boardService.get(boardId); // 해당 게시물 가져오기
		log.info("상세조회 할 게시물 :"+vo);
		
		model.addAttribute("board", vo); // get.jsp에 게시물 넘겨줌
		
		
		//## 좋아요 눌림 체크
		if(auth != null) { // 로그인이 되어있는 상태이면
			//해당 글의 boardId로 해당 글의 url을 가져옴 
			String url = vo.getBook().getUrl();
			UserDetails userDetails = (UserDetails) auth.getPrincipal(); // 현재 로그인한 유저
			String userid = userDetails.getUsername(); //로그인한 유저의 아이디
			boolean result = boardService.favoriteCheck(userid, url); //좋아요를 누른 상태이면 true 아니면 false
			model.addAttribute("favorite", result);
		}else { // 로그인이 안되어있는 상태라면 무조건 false
			model.addAttribute("favorite", false);
		}
		
	}
	
	@GetMapping("/modify")
	@PreAuthorize("isAuthenticated()")
	public void getmodify(Long boardId, Model model, @ModelAttribute("cri") Criteria cri) { //게시물 번호로 해당 게시물 가져옴
		log.info("boardId : " + boardId);
		log.info("pageNum : " + cri.getPageNum());
		log.info("amount : " + cri.getAmount());
		log.info("type : "+cri.getType());
		log.info("keyword : "+cri.getKeyword());
		
		BoardVO vo = boardService.get(boardId);
		log.info(vo);
		model.addAttribute("board", vo);
		//cri와 BoardVO가 modify.jsp로 전달
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String modify(BoardVO board,BookVO book,Criteria cri,RedirectAttributes rttr) { //게시글 수정 작업
		log.info("update.."+board);
		log.info("update.."+book);
		log.info("update.."+cri);
		
		
		if(boardService.modify(board, book)) { //게시글이 수정되면 result라는 이름으로 success라는 메세지 전송 
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		
		return "redirect:/board/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/remove")
	public String remove(Long boardId,Criteria cri,RedirectAttributes rttr) { //게시글 제거
		log.info("remove.."+boardId);
		log.info("remove.."+cri);

		
		if(boardService.remove(boardId)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/favorite")
	public ResponseEntity<String> addFavorite(@RequestBody FavoriteVO vo){ //좋아요 등록
		try {
			vo.setUserid(URLDecoder.decode(vo.getUserid(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Favorite : "+vo);
		
		boardService.favoriteRegister(vo);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/unFavorite") //좋아요 제거
	public ResponseEntity<Boolean> deleteFavorite(@RequestBody FavoriteVO vo){
		try {
			vo.setUserid(URLDecoder.decode(vo.getUserid(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Favorite : "+vo);
		
		boolean result = boardService.favoriteRemove(vo);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
