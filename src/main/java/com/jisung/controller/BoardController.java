package com.jisung.controller;

import java.awt.print.Book;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.BoardVO;
import com.jisung.domain.BookVO;
import com.jisung.domain.Criteria;
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
	public String list(Model model, Criteria cri) {
		log.info("메인화면");
		log.info("총 게시물 수 : " + boardService.total(cri));

		List<BoardVO> boardList = boardService.list(cri);
		model.addAttribute("list", boardList);
		model.addAttribute("pageMaker", new PageDTO(cri, boardService.total(cri)));
		return "/board/main";
	}

	@GetMapping("/register")
	public void register() {
		log.info("글 등록 접근");
	}

	@PostMapping("/register")
	public String register(BoardVO board, BookVO book, RedirectAttributes rttr) {
		log.info("상품등록처리");

		boardService.register(board, book);

		log.info(board);
		log.info(book);

		rttr.addFlashAttribute("result", board.getBoardId());

		return "redirect:/board/";
	}

	@GetMapping("/bookSearch")
	public void bookSearch() {
		log.info("북 검색페이지");

	}

	@GetMapping({"/get","/modify"})
	public void get(Long boardId, Model model, @ModelAttribute("cri") Criteria cri) {
		log.info("boardId : " + boardId);
		log.info("pageNum : " + cri.getPageNum());
		log.info("amount : " + cri.getAmount());
		BoardVO vo = boardService.get(boardId);
		log.info(vo);
		model.addAttribute("board", vo);
	}

	@PostMapping("/modify")
	public String modify(BoardVO board,BookVO book,Criteria cri,RedirectAttributes rttr) {
		log.info("update.."+board);
		log.info("update.."+book);
		log.info("update.."+cri);
		
		
		if(boardService.modify(board, book)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		
		return "redirect:/board/";
	}
	@PostMapping("/remove")
	public String remove(Long boardId,Criteria cri,RedirectAttributes rttr) {
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
}
