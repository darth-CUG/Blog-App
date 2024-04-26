package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.HashSet;
import java.util.Set;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SinglePostResponse {
	
	private String title;
	
	private String content;
	
	private String imageName;
	
	private String addedDate;
	
	private String categoryTitle;
	
	private String userName;
	
	private Set<SinglePostComments> comments = new HashSet<>();
	
	private int commentCount = AppConstants.ZERO_INTEGER;
	
	private int likesCount = AppConstants.ZERO_INTEGER;

}
