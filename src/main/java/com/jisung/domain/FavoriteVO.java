package com.jisung.domain;

import lombok.Data;

@Data
public class FavoriteVO {
	private Long favoriteId;
	private String userid;
	private String thumbnail;
	private String title;
	private String authors;
	private String url;
}
