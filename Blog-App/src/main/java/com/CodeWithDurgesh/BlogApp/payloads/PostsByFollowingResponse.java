package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsByFollowingResponse {
	private List<PostsByFollowingWrapper> content;
	private long totalElements;
	
}
