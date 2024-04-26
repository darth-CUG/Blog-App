package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentWrapper {
	
//	private int commentId;
	private String comment;
	private String byUser;
	private Date dateAdded;

}
