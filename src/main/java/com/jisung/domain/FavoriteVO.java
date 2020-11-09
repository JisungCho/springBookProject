package com.jisung.domain;

import lombok.Data;

@Data
public class FavoriteVO {
	private Long favoriteId;
	private String userid;
	private Long bookId;
}
