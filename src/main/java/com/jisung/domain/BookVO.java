package com.jisung.domain;

import java.util.List;

import lombok.Data;

@Data
public class BookVO {
	private Long bookId; //PK
	private Long boardId; //FK
	private String thumbnail;
	private String title;
	private String authors;
	private String url;
	
	//private List<FavoriteVO> favorite; //북마크 목록
}
