package com.jisung.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jisung.domain.Criteria;
import com.jisung.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class ReplyMapperTest {
	
	@Autowired
	private ReplyMapper replymapper;
	
	/*
	@Test
	public void insert() {
		ReplyVO vo = new ReplyVO();
		vo.setBoardId(33L);
		vo.setReply("댓글테스트1");
		vo.setReplyer("테스터1");
		replymapper.insert(vo);
	}
	*/
	/*
	@Test
	public void read() {
		ReplyVO vo = replymapper.read(1L);
		log.info(vo);
	}
	*/
	/*
	@Test
	public void delete() {
		replymapper.delete(1L);
	}
	*/
	/*
	@Test
	public void update() {
		ReplyVO vo = replymapper.read(2L);
		vo.setReply("업데이트");
		replymapper.update(vo);
	}
	*/
	/*
	@Test
	public void listpage() {
		Criteria cri = new Criteria();
		cri.setAmount(10);
		List<ReplyVO> list =  replymapper.listWithPaging(cri, 33L);
		list.forEach(reply -> log.info(reply));
	}
	*/
}

