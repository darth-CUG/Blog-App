package com.CodeWithDurgesh.BlogApp.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostsByFollowingWrapper {
	
	private int postId;
	private String postTitle;
	private String categoryTitle;
	private String writtenBy;
	private String postAddedDate;
	private String postImageName;
}
