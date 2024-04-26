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
public class SinglePostComments {

//private Integer commentId;
	
	private String comment;
	
	private String userName;
	
	private Date dateAdded;
}
