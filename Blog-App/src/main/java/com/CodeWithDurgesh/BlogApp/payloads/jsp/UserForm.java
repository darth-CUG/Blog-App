package com.CodeWithDurgesh.BlogApp.payloads.jsp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
private int userId;
	
	private String name;
	private String email;
	private String password;
	private String about; 
}
