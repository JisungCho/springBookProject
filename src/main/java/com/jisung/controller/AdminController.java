package com.jisung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jisung.domain.Criteria;
import com.jisung.domain.PageDTO;
import com.jisung.service.BoardService;
import com.jisung.service.MemberService;
import com.jisung.service.VisitorService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/admin")
@Log4j
public class AdminController {
	
	@Autowired
	VisitorService visitorService;
	@Autowired
	MemberService memberService;
	@Autowired
	BoardService boardService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@RequestMapping("/")
	public String test(Model model) {
		int today = visitorService.today();
		int yesterday = visitorService.yesterday();
		int twoDaysAgo = visitorService.twoDaysAgo();
		
		
		model.addAttribute("today", today);
		model.addAttribute("yesterday", yesterday); 
		model.addAttribute("twoDaysAgo", twoDaysAgo);
		
		//카카오 계정과 일반계정의 갯수 구하기
		model.addAttribute("kakao",memberService.MemberCount("ROLE_KAKAO"));
		model.addAttribute("member",memberService.MemberCount("ROLE_MEMBER"));
		
		//게시물 수 
		model.addAttribute("w_today", boardService.today());
		model.addAttribute("w_yesterday", boardService.yesterday());
		model.addAttribute("w_twoDaysAgo", boardService.twoDaysAgo()); 
		
		return "/admin/adMain";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@RequestMapping("/boardList")
	public String boardList(@ModelAttribute("cri") Criteria cri,Model model) {
		cri.setAmount(10);
		log.info("cri : "+cri);
		model.addAttribute("list", boardService.list(cri)); // 게시물 갯수
		model.addAttribute("pageMaker", new PageDTO(cri, boardService.total(cri))); //총 게시물 수와 pageNum , amount 전달
		return "/admin/boardList";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@PostMapping("/remove")
	public String remove(Criteria cri,RedirectAttributes rttr,String[] boardIdList) { //게시글 제거
		log.info("remove.."+cri);
		
		//선택삭제
		for(String id : boardIdList) {
			log.info("remove.."+id);
			if(boardService.remove(Long.parseLong(id))) {
				rttr.addFlashAttribute("result", "success");
			}
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/admin/boardList";
	}
}
