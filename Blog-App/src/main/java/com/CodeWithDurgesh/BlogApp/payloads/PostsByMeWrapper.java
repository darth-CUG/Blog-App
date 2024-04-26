package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostsByMeWrapper {
	private String postTitle;
	private String postContent;
//	private String postImageName;
	private Date postAddedDate;
	private String categoryTitle;
	private int commentsCount = AppConstants.ZERO_INTEGER;
	private int likesCount = AppConstants.ZERO_INTEGER;

}
