package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

	private Integer commentId;
	
	private String comment;
	
	private UserDto user;
	
	//why is postdto not here??
	
	private Date dateAdded;
}
