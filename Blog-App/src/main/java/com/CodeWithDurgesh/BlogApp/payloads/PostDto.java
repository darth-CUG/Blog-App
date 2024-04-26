package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.CodeWithDurgesh.BlogApp.config.AppConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;
	
	@Size(min = 4, message = "Title should be at least 4 characters long.")
	@NotBlank(message = "Title can not be blank.")
	private String title;
	
	@Size(min = 10, message = "Decription should be at least 10 characters long.")
	@NotBlank(message = "Content can not be blank.")
	private String content;
	
	@NotEmpty(message = "Please attach an image.")
	private String imageName;
	private Date addedDate;
	
	private CategoryDto category;
	
	private UserDto user;
	
	private Set<CommentDto> comments = new HashSet<>();
	
	private int commentCount = AppConstants.ZERO_INTEGER;
	
	private int likesCount = AppConstants.ZERO_INTEGER;
}
