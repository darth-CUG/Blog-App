package com.CodeWithDurgesh.BlogApp.payloads.jsp;

import java.util.Date;

import com.CodeWithDurgesh.BlogApp.payloads.CategoryDto;
import com.CodeWithDurgesh.BlogApp.payloads.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {
	
	private Integer postId;
	private String title;
	private String content;
	private String imageName;
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;

}
