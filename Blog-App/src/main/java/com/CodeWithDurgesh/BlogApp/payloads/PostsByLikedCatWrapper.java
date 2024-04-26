package com.CodeWithDurgesh.BlogApp.payloads;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostsByLikedCatWrapper {
	
	private int postId;
	private String postTitle;
	private String postContent;
	private String postImageName;
	private String postAddedDate;
	private String categoryTitle;
	private String writtenBy;
	private int commentsCount = AppConstants.ZERO_INTEGER;
	private int likesCount = AppConstants.ZERO_INTEGER;

	

}
