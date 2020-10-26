package com.jisung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jisung.domain.Criteria;
import com.jisung.domain.ReplyPageDTO;
import com.jisung.domain.ReplyVO;
import com.jisung.service.ReplyService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/reply/")
@Log4j
public class ReplyController {
	@Autowired
	private ReplyService replyService;
	
	//Json데이터를 받아서 사용하고 리턴값을 일반 문자열
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/new",consumes = "application/json",produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) { // 들어온 Json데이터를 reply객체로 변환
		log.info("댓글 등록  "+vo);
		int insertCount = replyService.register(vo);
		return insertCount == 1 ? new ResponseEntity<String>("등록완료", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	@GetMapping(value = "/pages/{boardId}/{page}",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("boardId") Long boardId){
		log.info("특정 게시물의 댓글 목록 확인 ");
		Criteria cri = new Criteria(page, 5);
		return new ResponseEntity<ReplyPageDTO>(replyService.getListPage(cri, boardId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{replyId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("replyId") Long replyId){
		log.info("댓글 상세 조회");
		return new ResponseEntity<ReplyVO>(replyService.get(replyId),HttpStatus.OK);
	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@PostMapping(value = "/{replyId}", produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo,@PathVariable("replyId") Long replyId){
		log.info("댓글 삭제");
		int removeCount = replyService.remove(vo);
		
		return removeCount == 1 ? new ResponseEntity<String>("삭제완료", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@RequestMapping(method = {RequestMethod.PUT , RequestMethod.PATCH},value="/{replyId}",
			consumes = "application/json", //json데이터가 들어옴
			produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> modify(@PathVariable("replyId") Long replyId,@RequestBody ReplyVO vo){
		log.info("댓글 수정");
		
		return replyService.modify(vo) == 1 ? new ResponseEntity<String>("수정완료", HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
