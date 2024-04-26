package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeDto {
	
	private int likeId;
	
	private int likeCount;
	
	private PostDto post;
	
	private UserDto user;
	 
	private Date addedDate;

}
