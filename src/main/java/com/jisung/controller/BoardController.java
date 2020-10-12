package com.jisung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/register")
	public void register() {
		log.info("글 등록 접근");
	}
	
	@PostMapping("/register")
	public String register(BoardVO board,BookVO book, RedirectAttributes rttr) {
		log.info("상품등록처리");
		
		boardService.register(board, book);
		
		log.info(board);
		log.info(book);
		
		return null;
	}
	
	@GetMapping("/bookSearch")
	public void bookSearch() {
		log.info("북 검색페이지");
	}
}
