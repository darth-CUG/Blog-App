package com.CodeWithDurgesh.BlogApp.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUserResponse {
	private String message;
	private FollowUserWrapper wrapper;

	

}
