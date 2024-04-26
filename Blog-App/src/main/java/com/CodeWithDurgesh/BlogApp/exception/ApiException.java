package com.CodeWithDurgesh.BlogApp.exception;

public class ApiException extends RuntimeException {

	String message;
		
	public ApiException(String message) {
		super(String.format("%s",message));
		this.message = message;
	}

}
