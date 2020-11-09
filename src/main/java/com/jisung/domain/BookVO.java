package com.jisung.domain;

import java.util.List;

import lombok.Data;

@Data
public class BookVO {
	private Long bookId;
	//private Long memberId;
	private Long boardId;
	private String thumbnail;
	private String title;
	private String authors;
	private String url;
	
	private List<FavoriteVO> favorite;
}
