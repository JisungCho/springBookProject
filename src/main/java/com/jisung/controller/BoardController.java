package com.jisung.controller;

import java.awt.print.Book;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
	
	//메인페이지
	@GetMapping("/") 
	public String list(Model model, Criteria cri) { //메인 화면 
		log.info("메인 페이지");
		log.info("총 게시물 수 : " + boardService.total(cri)); // 총 게시물 수 

		List<BoardVO> boardList = boardService.list(cri); //페이징 처리한 게시물 목록
		model.addAttribute("list", boardList); // 게시물목록을 list라는 이름으로 view에 전달할 것(검색조건이 있으면 처리)
		model.addAttribute("pageMaker", new PageDTO(cri, boardService.total(cri))); //페이징 조건 전달
		return "/board/main";
	}

	//글 등록 view로 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register") 
	public void register() { 
		log.info("글 등록 페이지 이동");
	}

	//글 등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String register(BoardVO board, BookVO book, RedirectAttributes rttr) {
		log.info("글 등록처리");
		
		boardService.register(board, book); // DB에 글등록 처리

		rttr.addFlashAttribute("result", board.getBoardId()); // result라는 이름으로 등록한 게시물의 번호를 전달

		return "redirect:/board/";
	}
	
	//책 검색 view로 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/bookSearch")
	public void bookSearch() { 
		log.info("책 검색페이지");

	}
	
	//게시물 상세조회 페이지로 이동
	@GetMapping("/get")
	public void get(Long boardId, Model model, @ModelAttribute("cri") Criteria cri,Authentication auth) { //게시물 상세조회
		
		BoardVO vo = boardService.get(boardId); // 게시물번호로 게시물 가져오기
		log.info("상세조회 할 게시물 :"+vo); 
		
		model.addAttribute("board", vo); // board란 이름으로 해당 게시물정보를 전달
		
		//현재로그인이 되어있거나 권한이 Admin이 아니면
		if(auth != null && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) { 
			log.info("권한 있음");
			
			//해당 책의 url
			String url = vo.getBook().getUrl();
			UserDetails userDetails = (UserDetails) auth.getPrincipal(); // 현재 로그인한 유저
			String userid = userDetails.getUsername(); //로그인한 유저의 아이디
			//좋아요 눌림 체크
			boolean result = boardService.favoriteCheck(userid, url); // 같은 책이 있다면 true 없다면 false
			model.addAttribute("favorite", result); 
		}else { // 로그인이 안되어있는 상태라면 무조건 북마크 설정 안되어있음
			log.info("권한 없음");
			model.addAttribute("favorite", false);
		}
	}
	
	//수정 view
	@GetMapping("/modify")
	@PreAuthorize("isAuthenticated()")
	public void getmodify(Long boardId, Model model, @ModelAttribute("cri") Criteria cri) { //게시물 번호로 해당 게시물 가져옴
		//get페이지에서 가지고있던 cri정보들
		log.info("boardId : " + boardId);
		log.info("pageNum : " + cri.getPageNum());
		log.info("amount : " + cri.getAmount());
		log.info("type : "+cri.getType());
		log.info("keyword : "+cri.getKeyword());
		
		BoardVO vo = boardService.get(boardId); //게시물 번호로 해당 게시물 정보를 가져옴
		model.addAttribute("board", vo); // board란 이름으로 해당 게시물 정보를 전달 
		//modify.jsp로 이동
	}
	
	//게시글 수정 작업
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String modify(BoardVO board,BookVO book,Criteria cri,RedirectAttributes rttr) { 
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

		//글 제거
		if(boardService.remove(boardId)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/";
	}
	
	//좋아요 등록
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/favorite")
	public ResponseEntity<Boolean> addFavorite(@RequestBody FavoriteVO vo){ 
		log.info("좋아요 등록");
		boolean result = boardService.favoriteRegister(vo);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	//좋아요 삭제
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/unFavorite") //좋아요 제거
	public ResponseEntity<Boolean> deleteFavorite(@RequestBody FavoriteVO vo){
		log.info("좋아요 제거");
		boolean result = boardService.favoriteRemove(vo);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
