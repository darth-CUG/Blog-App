package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsByLikedCatResponse {
	
	private List<PostsByLikedCatWrapper> content;
//	private long totalElements;
	private int totalPages; //changes for JSP pagination logic in feed.jsp
	private int currentPage;

}
