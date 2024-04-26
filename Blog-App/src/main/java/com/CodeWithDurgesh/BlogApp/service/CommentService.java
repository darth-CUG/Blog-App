package com.CodeWithDurgesh.BlogApp.service;

import com.CodeWithDurgesh.BlogApp.payloads.CommentDto;
import com.CodeWithDurgesh.BlogApp.payloads.CommentResponse;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
	
	void deleteComment(Integer commentId);
	
	CommentResponse getCommentsByPostId(Integer postId, Integer pageNo, Integer pageSize, String sortBy);
	
}
