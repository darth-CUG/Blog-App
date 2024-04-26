package com.CodeWithDurgesh.BlogApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CodeWithDurgesh.BlogApp.payloads.ApiResponse;
import com.CodeWithDurgesh.BlogApp.payloads.CommentDto;
import com.CodeWithDurgesh.BlogApp.service.CommentService;

@RestController
@RequestMapping("api/comments")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@PostMapping("createFor/post/{postId}/user/{userId}")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId){
		return new ResponseEntity<CommentDto>(commentService.createComment(commentDto, postId, userId),HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{commentId}")
	@PreAuthorize("hasRole(ROLE_ADMIN)")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable("commentId") Integer commentId){
		commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully.",true),HttpStatus.OK);
	}
}
