package com.jisung.domain;

import lombok.Data;

@Data
public class BookVO {
	private Long bookId;
	private Long boardId;
	private String thumbnail;
	private String title;
	private String authors;
	private String url;
}
