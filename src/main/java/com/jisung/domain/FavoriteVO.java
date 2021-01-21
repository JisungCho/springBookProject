package com.jisung.domain;

import lombok.Data;

@Data
public class FavoriteVO {
	private Long favoriteId; //PK
	private String userid; //FK
	private String thumbnail;
	private String title;
	private String authors;
	private String url;
}
