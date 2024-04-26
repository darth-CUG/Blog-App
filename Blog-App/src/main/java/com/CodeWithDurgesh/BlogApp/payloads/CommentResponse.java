package com.CodeWithDurgesh.BlogApp.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

	private List<CommentWrapper> content;
	private long totalElements;
}
